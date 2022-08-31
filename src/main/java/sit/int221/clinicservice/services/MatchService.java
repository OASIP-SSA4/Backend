package sit.int221.clinicservice.services;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sit.int221.clinicservice.dtos.MatchUserDTO;
import sit.int221.clinicservice.entities.User;
import sit.int221.clinicservice.exception.EventExceptionModel;
import sit.int221.clinicservice.exception.EventFieldError;
import sit.int221.clinicservice.repositories.UserRepository;

@Service
public class MatchService {
    private final UserRepository repository;
    private final ModelMapper modelMapper;

    Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

    @Autowired
    public MatchService(UserRepository repository, ModelMapper modelMapper){
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<Object> match(MatchUserDTO matchUserDTO){
        EventFieldError fieldError;
        EventExceptionModel eventExceptionModel;

        try {
            User user = repository.findByEmail(matchUserDTO.getEmail());
            Object sth = modelMapper.map(user, MatchUserDTO.class);
            boolean matchPassword = argon2.verify(user.getPassword(), matchUserDTO.getPassword());

            if (sth != null && matchPassword){
                return new ResponseEntity<>("Password Match", HttpStatus.OK);
            }else {
                fieldError = new EventFieldError("password", "password NOT Matched", HttpStatus.UNAUTHORIZED);
                eventExceptionModel = new EventExceptionModel(fieldError.getStatus(), "Attributes validation failed", fieldError);
                return new ResponseEntity<>(eventExceptionModel, fieldError.getStatus());
            }
        }catch (Exception ex){
            fieldError = new EventFieldError("email", "A user with the specified email DOES NOT exist", HttpStatus.NOT_FOUND);
            eventExceptionModel = new EventExceptionModel(fieldError.getStatus(), "Attributes validation failed", fieldError);
            return new ResponseEntity<>(eventExceptionModel, fieldError.getStatus());
        }
    }
}
