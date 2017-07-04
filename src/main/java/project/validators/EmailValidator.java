package project.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Pattern(regexp=".+@.+\\..+", message="{EmailValidator.email}")
@NotEmpty(message="{NotEmpty.userdto.email}")
@Size(max = 40, message="{Size.userdto.email}")
@Target({TYPE, FIELD, ANNOTATION_TYPE}) 
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface EmailValidator {
	String message() default "{EmailValidator.email}";
    Class<?>[] groups() default {}; 
    Class<? extends Payload>[] payload() default {};
}
