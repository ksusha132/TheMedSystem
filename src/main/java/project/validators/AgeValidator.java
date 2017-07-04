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
@Size(max = 3, message="The age is ni more than 3 simbols")
@Pattern(regexp="([0-9])", message="The age should include only numbers")
@Constraint(validatedBy = {})
@Target({TYPE, FIELD, ANNOTATION_TYPE, PARAMETER})
@Retention(RUNTIME)
@Documented
public @interface AgeValidator {
    String message() default "Name is incorrect";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
