package project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import project.security.CustomAuthenticationFailureHandler;
import project.security.services.CustomRememberMeService;
import project.security.services.CustomUserDetailService;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	public static final String REMEMBER_ME_KEY = "rememberme_key";
	public static final String REMEMBER_ME_PARAMETER = "rememberme";

	@Autowired
	CustomRememberMeService rememberMeService;

	@Autowired
	CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

	@Autowired
	CustomUserDetailService userDetailsService;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Bean
	public static ServletListenerRegistrationBean httpSessionEventPublisher() { 
		return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/", "users/**", "/users/register", "/users/registerConfirm.html", "/static/**", "/login", "/badToken")
				.permitAll().antMatchers("/admin/**").hasRole("ADMIN").antMatchers("/me").hasAnyRole("ADMIN", "USER")
				.anyRequest().authenticated().and()
				// Login
				.formLogin().loginProcessingUrl("/authenticate").loginPage("/login")
				.failureHandler(customAuthenticationFailureHandler).permitAll().and()
				// Logout
				.logout().deleteCookies("JSESSIONID").permitAll().and()
				// Remember me
				.rememberMe().rememberMeServices(rememberMeService).key(REMEMBER_ME_KEY)
				.rememberMeParameter(REMEMBER_ME_PARAMETER).and().exceptionHandling().accessDeniedPage("/error/403")
				.and()
				.sessionManagement()
				.maximumSessions(3)
				.expiredUrl("/login")
	            .sessionRegistry(sessionRegistry());
	}

}
