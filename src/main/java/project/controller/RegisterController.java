package project.controller;

import static project.services.UserServiceImpl.TOKEN_EXPIRED;
import static project.services.UserServiceImpl.TOKEN_VALID;
import static project.util.MsgUtil.EMAIL_ALREADY_EXISTS;
import static project.util.MsgUtil.LOGIN_ALREADY_EXISTS;
import static project.util.MsgUtil.UNDEFIND_TRY_AGAIN_ERROR;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import project.model.User;
import project.repository.ContactTypeRepository;
import project.repository.RoleRepository;
import project.repository.UserRepository;
import project.security.model.UserDTO;
import project.services.MailService;
import project.services.UserService;
import project.util.MsgUtil;
import project.util.SecurityUtil;

@Controller
@RequestMapping("/users")
public class RegisterController {


    private static final String MSG_VERIFICATION_EMAIL_SENT = "verification.emailSent";
    private static final String MSG_ACCOUNT_VERIFIED_SUCCESS = "message.accountVerified";
    private static final String MSG_VERIFICATION = "message.verification.";

    @Autowired
    SessionRegistry sessionRegistry;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MessageSource messageSource;

    @Autowired
    MailService mailService;

    @Autowired
    UserService userService;

    @Autowired
    ContactTypeRepository contactTypeRepository;

    @GetMapping("/register")
    public String registrationForm(WebRequest request, Model model) {

        // check if user already login and redirect him to home page if so
        if (SecurityUtil.getCurrentLogin() != null) {
            return "redirect:/";
        }

        UserDTO user = new UserDTO();
        model.addAttribute("user", user);

        return "registration";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") UserDTO user, BindingResult result, HttpServletRequest request,
                           RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "registration";
        }

        User u = new User(
                user.getFirstname(),
                user.getLastname(),
                user.getLogin(),
                user.getEmail(),
                passwordEncoder.encode(user.getPassword()));

        u.setRole(roleRepository.findByName("user"));

        String redirectionUrl = "redirect:/";
        try {

            u = userRepository.saveAndFlush(u);
            System.out.println("user was created: " + u.getPassword());

        } catch (DataIntegrityViolationException e) {
            if (userRepository.findByLogin(user.getLogin()) != null) {
                redirectAttributes.addFlashAttribute("statusError", MsgUtil.getMsg(LOGIN_ALREADY_EXISTS));
                return redirectionUrl + "users/register?error";
            }
            if (userRepository.findByEmail(user.getEmail()) != null) {
                redirectAttributes.addFlashAttribute("statusError", MsgUtil.getMsg(EMAIL_ALREADY_EXISTS));
                return redirectionUrl + "users/register?error";
            }
            redirectAttributes.addFlashAttribute("statusError", UNDEFIND_TRY_AGAIN_ERROR);
            return redirectionUrl + "users/register?error";
        }

        try {
            mailService.sendVerificationMail(u, getRequestUrl(request));
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("statusError", "There is a network problem, try again please");
            return redirectionUrl + "users/register?error";
        }
        redirectAttributes.addFlashAttribute("statusMessage", MsgUtil.getMsg(MSG_VERIFICATION_EMAIL_SENT));
        return redirectionUrl + "login?message";
    }

    @GetMapping("/registerConfirm")
    public String confirmRegistration(WebRequest request, Model model,
                                      @RequestParam("token") String token, RedirectAttributes redirectAttributes) {

        if (SecurityUtil.getCurrentLogin() != null) {
            SecurityContextHolder.clearContext();
        }

        final String result = userService.validateVerificationToken(token);

        if (TOKEN_VALID.equals(result)) {

            final User user = userService.getUserByToken(token);
            user.setEnabled(true);
            System.out.println(user);
            redirectAttributes.addFlashAttribute("statusMessage", MsgUtil.getMsg(MSG_ACCOUNT_VERIFIED_SUCCESS));

            return "redirect:/login?message";
        }
        redirectAttributes.addFlashAttribute("statusMessage", MsgUtil.getMsg(MSG_VERIFICATION + result));
        redirectAttributes.addFlashAttribute("token", token);
        redirectAttributes.addFlashAttribute("expired", TOKEN_EXPIRED.equals(result));
        return "redirect:/badToken";
    }

    @GetMapping("/resendToken")
    public String resendToken(HttpServletRequest request, Model model,
                              @RequestParam("token") String token, RedirectAttributes redirectAttributes) {

        try {
            mailService.sendVerificationMail(userService.getUserByToken(token), getRequestUrl(request));
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("statusError", "Some network problems, try again please");
            return "redirect:/users/register?error";
        }
        redirectAttributes.addFlashAttribute("statusMessage", MsgUtil.getMsg(MSG_VERIFICATION_EMAIL_SENT));
        return "redirect:/login?message";
    }

    private String getRequestUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
