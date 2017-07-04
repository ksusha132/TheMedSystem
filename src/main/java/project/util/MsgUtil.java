package project.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MsgUtil {

	public static final String LOGIN_ALREADY_EXISTS = "error.form.loginExists";
	public static final String EMAIL_ALREADY_EXISTS = "error.form.emailExists";
	public static final String UNDEFIND_TRY_AGAIN_ERROR = "error.form.undefind";
	public static final String BAD_CREDENTIALS = "error.badCredentials";

	public static final String REGISTER_SUCCES = "message.form.registerSuccess";

	
	private static MessageSource messageSource;

    @Autowired
    public MsgUtil(MessageSource messageSource) {
        MsgUtil.messageSource = messageSource;
    }
	
	public static String getMsg(final String source) {
		return messageSource.getMessage(source, null, LocaleContextHolder.getLocale());
	}
	
}
