package project.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import project.security.model.UserDTO;

public class PasswordConfirmValidator implements ConstraintValidator<PasswordConfirmValid, Object>{

	@Override
	public void initialize(PasswordConfirmValid constraintAnnotation) {}

	@Override
	public boolean isValid(Object o, ConstraintValidatorContext context) {
		UserDTO user = (UserDTO) o;
		return user.getPassword().equals(user.getConfirmPassword());
	}

}
