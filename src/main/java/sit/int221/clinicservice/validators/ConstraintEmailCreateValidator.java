package sit.int221.clinicservice.validators;

import org.springframework.beans.factory.annotation.Autowired;
import sit.int221.clinicservice.entities.User;
import sit.int221.clinicservice.repositories.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class ConstraintEmailCreateValidator implements ConstraintValidator<ConstraintEmailCreate, String> {
    @Autowired
    private UserRepository repository;

    @Override
    public void initialize(ConstraintEmailCreate constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext cxt) {
        try {
            List<User> userConstraint = repository.findConstraintEmailCreate(email);
            if (userConstraint.size() >= 1) {
                return false;
            } else {
                return true;
            }
        } catch (Exception ex) {
            return false;
        }
    }
}