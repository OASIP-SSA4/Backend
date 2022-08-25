package sit.int221.clinicservice.controllers;

import org.springframework.stereotype.Component;
import sit.int221.clinicservice.repositories.EventCategoryRepository;
import sit.int221.clinicservice.repositories.UserRepository;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

//@Component
//public class UniqueExceptionHandler implements ConstraintValidator<Unique, String> {
//
//    private final EventCategoryRepository eventCategoryRepository;
//    private final UserRepository userRepository;
//
//    public UniqueExceptionHandler(EventCategoryRepository repository, UserRepository userRepository){
//        this.eventCategoryRepository = repository;
//        this.userRepository = userRepository;
//    }
//
//    private String values;
//
//    @Override
//    public void initialize(Unique constraintAnnotation){
//        if (constraintAnnotation.useColumn().equals("name")){
//            values = "checkName";
//        }else if (constraintAnnotation.useColumn().equals("email")){
//            values = "checkEmail";
//        }else {
//            values = "checkCategoryName";
//        }
//    }
//
//    @Override
//    public boolean isValid(String newData, ConstraintValidatorContext context){
//        if (newData != null){
//            if(values.equals("checkName")){
//                return !userRepository.existsUserByName(newData.trim());
//            }else if (values.equals("checkEmail")){
//                return !userRepository.existsUserByEmail(newData.trim());
//            }
//            else {
//                return !eventCategoryRepository.existsEventCategoryByCategoryName(newData.trim());
//            }
//        }
//        return true;
//    }
//}
