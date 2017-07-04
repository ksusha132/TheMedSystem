package project.validators;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
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

@NotEmpty(message="The name should not be empty")
@Size(max = 25, message="The name should be less than 25 characters")
@Pattern(regexp="([A-Za-zА-Яа-я]+)", message="The name can contain only letters")
@Constraint(validatedBy = {})
@Target({TYPE, FIELD, ANNOTATION_TYPE, PARAMETER}) 
@Retention(RUNTIME)
@Documented
public @interface NameValidator {
	String message() default "Name is incorrect";
    Class<?>[] groups() default {}; 
    Class<? extends Payload>[] payload() default {};
}
