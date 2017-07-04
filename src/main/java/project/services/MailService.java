package project.services;

import java.io.IOException;

import project.model.User;

public interface MailService {
	void sendVerificationMail(User user, String url) throws IOException;
}
