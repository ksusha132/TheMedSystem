package project.services;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import project.linsteners.SendVerificationMailEvent;
import project.model.User;

@Service
public class MailServiceImpl implements MailService {
	
	@Autowired
	ApplicationEventPublisher eventPublisher;
	
	// could throw SocketTimeOutException and ConnectException
	@Override
	public void sendVerificationMail(final User user, final String url) throws IOException{
		eventPublisher.publishEvent(new SendVerificationMailEvent(user, url));
	}

}
