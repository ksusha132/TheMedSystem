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

@NotEmpty(message = "The second name should not be empty")
@Size(max = 25, message = "The second name should be less than 25 characters")
@Pattern(regexp = "([A-Za-zА-Яа-я]+)", message = "The second name can contain only letters") //?
@Constraint(validatedBy = {})
@Target({TYPE, FIELD, ANNOTATION_TYPE, PARAMETER})
@Retention(RUNTIME)
@Documented
public @interface SecondNameValidator {
    String message() default "Name is incorrect";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
