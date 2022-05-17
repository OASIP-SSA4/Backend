package sit.int221.clinicservice.controllers;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ValidationHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request){
        Map<String, Object> errors = new HashMap<>();
        errors.put("message", "Event attributes validation failed!");
        Map<String, String> errorFields = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error)->{
            String name = ((FieldError) error).getField();
            String defaultMessage = error.getDefaultMessage();
            errorFields.put(name,defaultMessage);
        });
        errors.put("fields",errorFields);
        return new ResponseEntity<Object>(errors,HttpStatus.BAD_REQUEST);
    }
}
