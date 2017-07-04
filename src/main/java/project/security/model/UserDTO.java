package project.security.model;

import project.validators.EmailValidator;
import project.validators.LoginValidator;
import project.validators.NameValidator;
import project.validators.PasswordConfirmValid;
import project.validators.PasswordValidator;

@PasswordConfirmValid
public class UserDTO {
	
public UserDTO() {}
	
	@LoginValidator
    private String login;
	
    @EmailValidator
    private String email;
    
	@PasswordValidator
    private String password;
	
	@NameValidator
	private String firstname;
	
	@NameValidator
	private String lastname;
	
	private String confirmPassword;
    
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

}
