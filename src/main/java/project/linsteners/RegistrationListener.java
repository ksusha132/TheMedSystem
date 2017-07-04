package project.linsteners;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import project.model.User;
import project.services.UserService;
import project.util.MsgUtil;

@Component
public class RegistrationListener implements ApplicationListener<SendVerificationMailEvent>{

	@Autowired
	UserService userService;
	
	@Autowired
	JavaMailSender mailSender;
		
	@Override
	public void onApplicationEvent(SendVerificationMailEvent event) {

	     sendVerificationMail(event);
	}
	
	private void sendVerificationMail(SendVerificationMailEvent event) {
		final User user = event.getUser();
        final String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);
        final SimpleMailMessage email = constructEmailMessage(event, user, token);
        mailSender.send(email);
        
	}
	
	private final SimpleMailMessage constructEmailMessage(final SendVerificationMailEvent event, final User user, final String token) {
        final String confirmationUrl = event.getAppUrl() + "/users/registerConfirm.html?token=" + token;
        final String message = MsgUtil.getMsg("message.mail.registration");
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setFrom("theturboenglish@gmail.com");
        email.setSubject("Registration Confirmation");
        email.setText(message + " \r\n" + confirmationUrl);
        return email;
    }

}
