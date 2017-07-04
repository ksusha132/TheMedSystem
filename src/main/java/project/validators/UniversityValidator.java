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

@NotEmpty(message = "The university should not be empty")
@Size(max = 25, message = "The university should be less than 30 characters")
@Pattern(regexp = "([A-Za-zА-Яа-я]+)", message = "The university can contain only letters") //?
@Constraint(validatedBy = {})
@Target({TYPE, FIELD, ANNOTATION_TYPE, PARAMETER})
@Retention(RUNTIME)
@Documented
public @interface UniversityValidator {
    String message() default "Name is incorrect";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
