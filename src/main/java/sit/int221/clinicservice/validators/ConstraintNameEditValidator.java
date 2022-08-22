package sit.int221.clinicservice.validators;

import org.springframework.beans.factory.annotation.Autowired;
import sit.int221.clinicservice.entities.User;
import sit.int221.clinicservice.repositories.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

//public class ConstraintNameEditValidator implements ConstraintValidator<ConstraintNameEdit, String> {
//@Autowired
//private UserRepository repository;
//
//@Override
//public void initialize(ConstraintNameEdit constraintAnnotation) {}
//
//@Override
//public boolean isValid(String name, ConstraintValidatorContext cxt) {
//        try {
//        List<User> userConstraint = repository.findConstraintNameEdit(name);
//        if (userConstraint.size() >= 1) {return false;}
//        else{return true;}
//        }catch(Exception ex) {
//            return false;
//        }
//    }
//}
