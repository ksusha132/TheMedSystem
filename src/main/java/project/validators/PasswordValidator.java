package project.validators;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Pattern(regexp="([A-Za-zА-Яа-я0-9_-]+)", message="The password can contain only letters, numbers, underscopes and dashes")
@NotEmpty(message="{NotEmpty.userdto.password}")
@Size(min = 5, max = 24, message="{Size.userdto.password}")
@Target({TYPE, FIELD, ANNOTATION_TYPE}) 
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface PasswordValidator {
	String message() default "";
    Class<?>[] groups() default {}; 
    Class<? extends Payload>[] payload() default {};
}
