package project.linsteners;

import org.springframework.context.ApplicationEvent;

import project.model.User;


public class SendVerificationMailEvent extends ApplicationEvent {

	private static final long serialVersionUID = 4587680617409292120L;
	
	private final String appUrl;
	private final User user;

	public SendVerificationMailEvent(final User user, final String appUrl) {
		super(user);
		this.user = user;
		this.appUrl = appUrl;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public User getUser() {
		return user;
	}
}
