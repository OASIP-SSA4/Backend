package sit.int221.clinicservice.services;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sit.int221.clinicservice.dtos.MatchUserDTO;
import sit.int221.clinicservice.entities.User;
import sit.int221.clinicservice.exception.EventExceptionModel;
import sit.int221.clinicservice.exception.EventFieldError;
import sit.int221.clinicservice.repositories.UserRepository;
import sit.int221.clinicservice.tokens.JwtResponse;
import sit.int221.clinicservice.tokens.JwtTokenUtil;

import java.util.ArrayList;

@Getter
@Setter
@Service
public class MatchService  implements UserDetailsService {
    private final UserRepository repository;
    private final ModelMapper modelMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

    @Autowired
    public MatchService(UserRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    // เช็ค Mail + Password
    public ResponseEntity<Object> match(MatchUserDTO matchUserDTO) {
        EventFieldError fieldError;
        EventExceptionModel eventExceptionModel;

        try {
            User user = repository.findByEmail(matchUserDTO.getEmail());
            Object sth = modelMapper.map(user, MatchUserDTO.class);
            boolean matchPassword = argon2.verify(user.getPassword(), matchUserDTO.getPassword());

            // กรณีที่ถูกต้อง status 200
            if (sth != null && matchPassword) {
                UserDetails userDetails = loadUserByUsername(matchUserDTO.getEmail());
                String token = jwtTokenUtil.generateToken(userDetails);
                return new ResponseEntity<>( new JwtResponse("Login Successful","Password Matched", token), HttpStatus.OK);
            } else {
                // กรณีที่ mail ถูก password ผิด status 401
                fieldError = new EventFieldError("password", "Password NOT Matched", HttpStatus.UNAUTHORIZED);
                eventExceptionModel = new EventExceptionModel(fieldError.getStatus(), "Attributes validation failed !!!", fieldError);
                return new ResponseEntity<>( eventExceptionModel, fieldError.getStatus());
            }
        } catch (Exception ex) {
            // กรณีที่ไม่มี mail ในระบบ status 404
            fieldError = new EventFieldError("email", "A user with the specified email DOES NOT exist", HttpStatus.NOT_FOUND);
            eventExceptionModel = new EventExceptionModel(fieldError.getStatus(), "Attributes validation failed !!!", fieldError);
            return new ResponseEntity<>( eventExceptionModel, fieldError.getStatus());
        }
    }

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }
}
