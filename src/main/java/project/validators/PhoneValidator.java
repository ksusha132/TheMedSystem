package project.validators;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@NotEmpty(message="The age should not be empty")
@Size(max = 12, message="The age is no more than 12 simbols")
@Pattern(regexp="([0-9])", message="The age should include only numbers")
@Constraint(validatedBy = {})
@Target({TYPE, FIELD, ANNOTATION_TYPE, PARAMETER})
@Retention(RUNTIME)
@Documented
public @interface PhoneValidator {
        String message() default "Name is incorrect";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
}
