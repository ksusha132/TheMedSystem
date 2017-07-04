package project.controller;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javassist.NotFoundException;
import project.model.ContactInfo;
import project.model.ContactType;
import project.model.User;
import project.repository.ContactInfoRepository;
import project.repository.ContactTypeRepository;
import project.repository.RoleRepository;
import project.repository.UserRepository;
import project.services.MailService;
import project.services.UserService;
import project.util.MsgUtil;
import project.util.SecurityUtil;
import project.validators.LoginValidator;

import static project.util.MsgUtil.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Controller
@Transactional
@RequestMapping("/users")
@Validated
public class UserController {

    private static final String UPLOADED_FOLDER = new File("").getAbsolutePath()
            .concat("/src/main/webapp/images/users/");

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

    @Autowired
    private ContactInfoRepository contactInfoRepository;

    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @GetMapping("/{id_user}")
    public String getUser(@PathVariable("id_user") Long id_user, Model model) throws NotFoundException {

        project.dto.UserDTO userDTO = userService.getUserDTO(id_user);

        Iterable<ContactType> contactTypeList = contactTypeRepository.findAll();
        model.addAttribute("user", userDTO);
        model.addAttribute("contact_types", contactTypeList);

        return "user";

    }

    @GetMapping("/me")
    public String getMe(Model model) throws NotFoundException {
        String username = SecurityUtil.getCurrentLogin();
        project.dto.UserDTO userDTO = userService.getUserDTO(username);

        Iterable<ContactType> contactTypeList = contactTypeRepository.findAll();
        model.addAttribute("user", userDTO);
        model.addAttribute("contact_types", contactTypeList);

        return "user";
    }

    @PostMapping(path = "/update_user/{id_user}")
    public String updateUser(@PathVariable Long id_user, @RequestParam String name, @RequestParam String last_name,
                             @RequestParam String address, @RequestParam String patronymic, @RequestParam Integer age,
                             @RequestParam String phone_num, @RequestParam String police_num,
                             @RequestParam(required = false) Integer gender, @RequestParam(required = false) Integer id_role,
                             RedirectAttributes redirectAttributes,
                             HttpServletRequest request) {

        List<String> messages = userService.updateUser(id_user, name, last_name, age, gender, id_role, address,
                phone_num, police_num, patronymic);
        redirectAttributes.addFlashAttribute("messages", messages);
        return "redirect:" + request.getHeader("Referer");
    }

    @PostMapping(path = "/add_contact/{id_user}")
    public
    @ResponseBody
    String addNewConact(@PathVariable Long id_user, @RequestParam String value,
                        @RequestParam Integer id_contact_type) {
        if (!value.isEmpty()) {
            ContactInfo ci = new ContactInfo();
            ci.setValue(value);
            ContactType contactType = contactTypeRepository.findOne(id_contact_type);
            ci.setContactType(contactType);
            User user = userRepository.findOne(id_user);
            ci.setUser(user);
            contactInfoRepository.save(ci);
            return "Saved";
        } else {
            return "the value is empty";
        }
    }

    @PostMapping(path = "/delete_contact/{id_contact_info}")
    public
    @ResponseBody
    String deleteUserContact(@PathVariable Integer id_contact_info) {
        ContactInfo ci = contactInfoRepository.findOne(id_contact_info);
        contactInfoRepository.delete(ci);
        return "Deleted";
    }

    @PostMapping("/{id}/updatePhoto")
    public String updatePhoto(@PathVariable Long id, @RequestParam("photo") MultipartFile file,
                              HttpServletRequest request, RedirectAttributes redirectAttributes) {

        User user = userRepository.findOne(id);
        String photo = "";
        if (file != null && !file.isEmpty()) {
            try {

                // Get the file and save it somewhere
                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOADED_FOLDER + id + '.' + file.getOriginalFilename().split("\\.")[1]);
                Files.write(path, bytes);
                photo = id + "." + file.getOriginalFilename().split("\\.")[1];

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            photo = user.getPhoto();
        }

        user.setPhoto(photo);
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("messages", "Photo was successfully updated");

        return "redirect:" + request.getHeader("Referer");

    }

    @PostMapping("/{id}/deletePhoto")
    @ResponseBody
    public String deletePhoto(@PathVariable Long id, HttpServletRequest request) {

        User user = userRepository.findOne(id);
        user.setPhoto(null);
        userRepository.save(user);
        return "deleted";
    }

    @PostMapping("/{id}/updateLogin")
    public String updateLogin(@PathVariable Long id, @RequestParam("old-login") String oldLogin,
                              @LoginValidator @RequestParam("new-login") String newLogin, HttpServletRequest request,
                              RedirectAttributes redirectAttributes) {

        String redirectUrl = "redirect:" + request.getHeader("Referer");

        // check if old login equals new login
        if (oldLogin.equals(newLogin)) {
            return redirectUrl;
        }

        // check if user already in database
        if (userRepository.findByLogin(newLogin) != null) {
            redirectAttributes.addFlashAttribute("errors", MsgUtil.getMsg(LOGIN_ALREADY_EXISTS));
            return redirectUrl;
        }

        SecurityUtil.expireUserSessions(oldLogin);
        User updatable = userRepository.findByLogin(oldLogin);
        updatable.setLogin(newLogin);
        userRepository.save(updatable);
        redirectAttributes.addFlashAttribute("messages", "Login has been successfully updated");
        return redirectUrl;

    }

}