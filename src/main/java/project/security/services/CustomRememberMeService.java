package project.security.services;

import static project.WebSecurityConfig.REMEMBER_ME_KEY;
import static project.WebSecurityConfig.REMEMBER_ME_PARAMETER;

import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import project.repository.TokenRepository;
import project.security.model.Token;

@Service
public class CustomRememberMeService implements RememberMeServices, InitializingBean, LogoutHandler {

	public static final String SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY = "remember-me";
	private static final int TOKEN_VALIDITY_SECONDS = 60  * 60 * 24;
    private static final int DEFAULT_SERIES_LENGTH = 16;
    private static final int DEFAULT_TOKEN_LENGTH = 16;
	private static final String DELIMITER = ":";

	// ~ Instance fields
	// ================================================================================================
	protected final Log logger = LogFactory.getLog(getClass());

	protected final MessageSourceAccessor messages = SpringSecurityMessageSource
			.getAccessor();

	private UserDetailsService userDetailsService;
	private UserDetailsChecker userDetailsChecker = new AccountStatusUserDetailsChecker();
	private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();

	private String cookieName = SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY;
	private String cookieDomain;
	private String parameter;
	private boolean alwaysRemember;
	private String key;
	private int tokenValiditySeconds = TOKEN_VALIDITY_SECONDS;
	private Boolean useSecureCookie = null;
	private Method setHttpOnlyMethod;
	private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
    private SecureRandom random;

    @Autowired
    private TokenRepository tokenRepository;

	@Autowired
    public CustomRememberMeService(Environment env, UserDetailsService userDetailsService) {
		Assert.notNull(userDetailsService, "UserDetailsService cannot be null");
		this.key = REMEMBER_ME_KEY;
		this.userDetailsService = userDetailsService;
		this.parameter = REMEMBER_ME_PARAMETER;
		this.setHttpOnlyMethod = ReflectionUtils.findMethod(Cookie.class, "setHttpOnly",
				boolean.class);
        random = new SecureRandom();
    }

	public void afterPropertiesSet() throws Exception {
		Assert.hasLength(key, "key cannot be empty or null");
		Assert.notNull(userDetailsService, "A UserDetailsService is required");
	}

	public Authentication autoLogin(HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("start auto login");
		
		String rememberMeCookie = extractRememberMeCookie(request);

		if (rememberMeCookie == null) {
			return null;
		}

		logger.debug("Remember-me cookie detected");

		if (rememberMeCookie.length() == 0) {
			logger.debug("Cookie was empty");
			cancelCookie(request, response);
			return null;
		}

		UserDetails user = null;

		try {
			String[] cookieTokens = decodeCookie(rememberMeCookie);
			user = processAutoLoginCookie(cookieTokens, request, response);
			userDetailsChecker.check(user);

			logger.debug("Remember-me cookie accepted");

			return createSuccessfulAuthentication(request, user);
		}
		catch (CookieTheftException cte) {
			cancelCookie(request, response);
			throw cte;
		}
		catch (UsernameNotFoundException noUser) {
			logger.debug("Remember-me login was valid but corresponding user not found.",
					noUser);
		}
		catch (InvalidCookieException invalidCookie) {
			logger.debug("Invalid remember-me cookie: " + invalidCookie.getMessage());
		}
		catch (AccountStatusException statusInvalid) {
			logger.debug("Invalid UserDetails: " + statusInvalid.getMessage());
		}
		catch (RememberMeAuthenticationException e) {
			logger.debug(e.getMessage());
		}

		cancelCookie(request, response);
		return null;
	}

	protected String extractRememberMeCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();

		if ((cookies == null) || (cookies.length == 0)) {
			return null;
		}

		for (Cookie cookie : cookies) {
			if (cookieName.equals(cookie.getName())) {
				return cookie.getValue();
			}
		}

		return null;
	}

	protected Authentication createSuccessfulAuthentication(HttpServletRequest request,
			UserDetails user) {
		RememberMeAuthenticationToken auth = new RememberMeAuthenticationToken(key, user,
				authoritiesMapper.mapAuthorities(user.getAuthorities()));
		auth.setDetails(authenticationDetailsSource.buildDetails(request));
		return auth;
	}

	protected String[] decodeCookie(String cookieValue) throws InvalidCookieException {
		for (int j = 0; j < cookieValue.length() % 4; j++) {
			cookieValue = cookieValue + "=";
		}

		if (!Base64.isBase64(cookieValue.getBytes())) {
			throw new InvalidCookieException(
					"Cookie token was not Base64 encoded; value was '" + cookieValue
							+ "'");
		}

		String cookieAsPlainText = new String(Base64.decode(cookieValue.getBytes()));

		String[] tokens = StringUtils.delimitedListToStringArray(cookieAsPlainText,
				DELIMITER);

		if ((tokens[0].equalsIgnoreCase("http") || tokens[0].equalsIgnoreCase("https"))
				&& tokens[1].startsWith("//")) {
			// Assume we've accidentally split a URL (OpenID identifier)
			String[] newTokens = new String[tokens.length - 1];
			newTokens[0] = tokens[0] + ":" + tokens[1];
			System.arraycopy(tokens, 2, newTokens, 1, newTokens.length - 1);
			tokens = newTokens;
		}

		return tokens;
	}

	protected String encodeCookie(String[] cookieTokens) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cookieTokens.length; i++) {
			sb.append(cookieTokens[i]);

			if (i < cookieTokens.length - 1) {
				sb.append(DELIMITER);
			}
		}

		String value = sb.toString();

		sb = new StringBuilder(new String(Base64.encode(value.getBytes())));

		while (sb.charAt(sb.length() - 1) == '=') {
			sb.deleteCharAt(sb.length() - 1);
		}

		return sb.toString();
	}

	public void loginFail(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("Interactive login attempt was unsuccessful.");
		cancelCookie(request, response);
		onLoginFail(request, response);
	}

	protected void onLoginFail(HttpServletRequest request, HttpServletResponse response) {
	}

	public void loginSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication successfulAuthentication) {

		if (!rememberMeRequested(request, parameter)) {
			logger.debug("Remember-me login not requested.");
			return;
		}

		onLoginSuccess(request, response, successfulAuthentication);
	}

	protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
		if (alwaysRemember) {
			return true;
		}

		String paramValue = request.getParameter(parameter);

		if (paramValue != null) {
			if (paramValue.equalsIgnoreCase("true") || paramValue.equalsIgnoreCase("on")
					|| paramValue.equalsIgnoreCase("yes") || paramValue.equals("1")) {
				return true;
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Did not send remember-me cookie (principal did not set parameter '"
					+ parameter + "')");
		}

		return false;
	}

	/**
	 * Sets a "cancel cookie" (with maxAge = 0) on the response to disable persistent
	 * logins.
	 */
	protected void cancelCookie(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("Cancelling cookie");
		Cookie cookie = new Cookie(cookieName, null);
		cookie.setMaxAge(0);
		cookie.setPath(getCookiePath(request));
		if (cookieDomain != null) {
			cookie.setDomain(cookieDomain);
		}
		response.addCookie(cookie);
	}

	protected void setCookie(String[] tokens, int maxAge, HttpServletRequest request,
			HttpServletResponse response) {
		String cookieValue = encodeCookie(tokens);
		Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setMaxAge(maxAge);
		cookie.setPath(getCookiePath(request));
		if (cookieDomain != null) {
			cookie.setDomain(cookieDomain);
		}
		if (maxAge < 1) {
			cookie.setVersion(1);
		}

		if (useSecureCookie == null) {
			cookie.setSecure(request.isSecure());
		}
		else {
			cookie.setSecure(useSecureCookie);
		}

		if (setHttpOnlyMethod != null) {
			ReflectionUtils.invokeMethod(setHttpOnlyMethod, cookie, Boolean.TRUE);
		}
		else if (logger.isDebugEnabled()) {
			logger.debug("Note: Cookie will not be marked as HttpOnly because you are not using Servlet 3.0 (Cookie#setHttpOnly(boolean) was not found).");
		}

		response.addCookie(cookie);
	}

	private String getCookiePath(HttpServletRequest request) {
		String contextPath = request.getContextPath();
		return contextPath.length() > 0 ? contextPath : "/";
	}

	
	public void setCookieName(String cookieName) {
		Assert.hasLength(cookieName, "Cookie name cannot be empty or null");
		this.cookieName = cookieName;
	}

	public void setCookieDomain(String cookieDomain) {
		Assert.hasLength(cookieDomain, "Cookie domain cannot be empty or null");
		this.cookieDomain = cookieDomain;
	}

	protected String getCookieName() {
		return cookieName;
	}

	public void setAlwaysRemember(boolean alwaysRemember) {
		this.alwaysRemember = alwaysRemember;
	}

	/**
	 * Sets the name of the parameter which should be checked for to see if a remember-me
	 * has been requested during a login request. This should be the same name you assign
	 * to the checkbox in your login form.
	 *
	 * @param parameter the HTTP request parameter
	 */
	public void setParameter(String parameter) {
		Assert.hasText(parameter, "Parameter name cannot be empty or null");
		this.parameter = parameter;
	}

	public String getParameter() {
		return parameter;
	}

	protected UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}

	public String getKey() {
		return key;
	}

	public void setTokenValiditySeconds(int tokenValiditySeconds) {
		this.tokenValiditySeconds = tokenValiditySeconds;
	}

	protected int getTokenValiditySeconds() {
		return tokenValiditySeconds;
	}

	public void setUseSecureCookie(boolean useSecureCookie) {
		this.useSecureCookie = useSecureCookie;
	}

	protected AuthenticationDetailsSource<HttpServletRequest, ?> getAuthenticationDetailsSource() {
		return authenticationDetailsSource;
	}

	public void setAuthenticationDetailsSource(
			AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
		Assert.notNull(authenticationDetailsSource,
				"AuthenticationDetailsSource cannot be null");
		this.authenticationDetailsSource = authenticationDetailsSource;
	}

	public void setUserDetailsChecker(UserDetailsChecker userDetailsChecker) {
		this.userDetailsChecker = userDetailsChecker;
	}

	public void setAuthoritiesMapper(GrantedAuthoritiesMapper authoritiesMapper) {
		this.authoritiesMapper = authoritiesMapper;
	}
 
    @Transactional
    protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request, HttpServletResponse response) {
    	System.out.println("start processing auto login cookie");
        Token token = getPersistentToken(cookieTokens);
        String login = token.getUserLogin();

        // Token also matches, so login is valid. Update the token value, keeping the *same* series number.
        token.setDate(new Date());
        token.setValue(generateTokenData());
        token.setIpAddress(request.getRemoteAddr());
        token.setUserAgent(request.getHeader("User-Agent"));
        try {
            tokenRepository.save(token);
            addCookie(token, request, response);
        } catch (DataAccessException e) {
            //log.error("Failed to update token: ", e);
            throw new RememberMeAuthenticationException("Autologin failed due to data access problem", e);
        }
        
       // return userDetailService.loadUserByUsername(login);
        return getUserDetailsService().loadUserByUsername(login);
    }
    
 
    protected void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        System.out.println("on login succes");
        
        Token token = new Token();
        token.setSeries(generateSeriesData());
        token.setUserLogin(authentication.getName());
        token.setValue(generateTokenData());
        token.setDate(new Date());
        token.setIpAddress(request.getRemoteAddr());
        token.setUserAgent(request.getHeader("User-Agent"));
        try {
            tokenRepository.save(token);
            addCookie(token, request, response);
        } catch (DataAccessException e) {
           // log.error("Failed to save persistent token ", e);
        }
    }
    
    /**
     * When logout occurs, only invalidate the current token, and not all user sessions.
     * 
     */
    @Override
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        cancelCookie(request, response);

    	String rememberMeCookie = extractRememberMeCookie(request);
        if (rememberMeCookie != null && rememberMeCookie.length() != 0) {
            try {
                String[] cookieTokens = decodeCookie(rememberMeCookie);
                Token token = getPersistentToken(cookieTokens);
                tokenRepository.delete(token.getSeries());
            } catch (InvalidCookieException ice) {
                //log.info("Invalid cookie, no persistent token could be deleted");
            } catch (RememberMeAuthenticationException rmae) {
                //log.debug("No persistent token found, so no token could be deleted");
            }
        }
        
    }

    /**
     * Validate the token and return it.
     */
    private Token getPersistentToken(String[] cookieTokens) {
        if (cookieTokens.length != 2) {
            throw new InvalidCookieException("Cookie token did not contain " + 2 +
                    " tokens, but contained '" + Arrays.asList(cookieTokens) + "'");
        }

        final String presentedSeries = cookieTokens[0];
        final String presentedToken = cookieTokens[1];

        Token token = null;
        try {
            token = tokenRepository.findOne(presentedSeries);
        } catch (DataAccessException e) {
            //log.error("Error to access database", e );
        }

        if (token == null) {
            // No series match, so we can't authenticate using this cookie
            throw new RememberMeAuthenticationException("No persistent token found for series id: " + presentedSeries);
        }

        // We have a match for this user/series combination
        if (!presentedToken.equals(token.getValue())) {
            // Token doesn't match series value. Delete this session and throw an exception.
            tokenRepository.delete(token.getSeries());
            throw new CookieTheftException("Invalid remember-me token (Series/token) mismatch. Implies previous cookie theft attack.");
        }
        
        if (DateUtils.addSeconds(token.getDate(), TOKEN_VALIDITY_SECONDS).before(new Date())) {
        	// Token expire. Delete this session and throw an exception
        	System.out.println("delete token " + token.getSeries());
            tokenRepository.delete(token.getSeries());
            throw new RememberMeAuthenticationException("Remember-me login has expired");
        }
        return token;
    }

    private String generateSeriesData() {
        byte[] newSeries = new byte[DEFAULT_SERIES_LENGTH];
        random.nextBytes(newSeries);
        return new String(Base64.encode(newSeries));
    }

    private String generateTokenData() {
        byte[] newToken = new byte[DEFAULT_TOKEN_LENGTH];
        random.nextBytes(newToken);
        return new String(Base64.encode(newToken));
    }

    private void addCookie(Token token, HttpServletRequest request, HttpServletResponse response) {
        setCookie(
                new String[]{token.getSeries(), token.getValue()},
                TOKEN_VALIDITY_SECONDS, request, response);
    }
}
