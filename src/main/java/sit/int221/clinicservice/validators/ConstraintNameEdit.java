package sit.int221.clinicservice.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;

//@Documented
//@Constraint(validatedBy = ConstraintNameEditValidator.class)
//@Retention(RetentionPolicy.RUNTIME)
//@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
//@ReportAsSingleViolation
//public @interface ConstraintNameEdit {
//    String message() default "This name is registered. Enter a new name !";
//    Class<?>[] groups() default {};
//    Class<? extends Payload>[] payload() default {};
//}
