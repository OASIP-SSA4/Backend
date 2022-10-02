package sit.int221.clinicservice.services;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.clinicservice.dtos.CreateUserDTO;
import sit.int221.clinicservice.dtos.EditUserDTO;
import sit.int221.clinicservice.dtos.MatchUserDTO;
import sit.int221.clinicservice.dtos.UserDTO;
import sit.int221.clinicservice.entities.User;
import sit.int221.clinicservice.repositories.UserRepository;
import sit.int221.clinicservice.tokens.JwtResponse;
import sit.int221.clinicservice.tokens.JwtTokenUtil;
import sit.int221.clinicservice.validators.HandleValidationErrors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private AuthenticationManager authenticationManager;

    Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

    public User save(CreateUserDTO createUserDTO){
        User user = modelMapper.map(createUserDTO, User.class);
        user.setName(createUserDTO.getName().trim());
        user.setEmail(createUserDTO.getEmail().trim());
        return repository.saveAndFlush(user);
    }

    public List<UserDTO> getAllUser(){
        List<User> userList = repository.findAll((Sort.by("name").ascending()));
        return listMapper.mapList(userList, UserDTO.class, modelMapper);
    }

    @Autowired
    private UserService(UserRepository repository) {
        this.repository = repository;
    }

    public UserDTO getUserById(Integer id){
        User user = repository.findById(id)
                .orElseThrow(()->new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User id"+ id +
                        "Does Not Exist"
                ));
        return modelMapper.map(user, UserDTO.class);
    }

    public void checkConstraintNameUser(String name, Integer id){
        List<User> sameNameUser = repository.findConstraintNameUpdate(name,id);
        if(sameNameUser.size()>=1){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"This name have been used, please update new name");
        }
    }

    public void checkConstraintEmailUser(String email, Integer id){
        List<User> sameEmailUser = repository.findConstraintEmailUpdate(email,id);
        if(sameEmailUser.size()>=1){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"This email have been used, please update new email");
        }
    }

    public UserDTO updateUser(@Valid EditUserDTO updateUser, Integer id) {
        List<User> isOldName = repository.findIsOldName(updateUser.getName(),updateUser.getEmail(),id);
        if(isOldName.size()>=1){
            return modelMapper.map(updateUser, UserDTO.class);
        }
        checkConstraintEmailUser(updateUser.getEmail(),id);
        checkConstraintNameUser(updateUser.getName(),id);
        User editUser = repository.findById(id).map(user -> {
            if(updateUser.getName() == null || updateUser.getName().trim() == ""){updateUser.setName(user.getName());}
            user.setName(updateUser.getName().trim());
            if(updateUser.getEmail() == null || updateUser.getEmail().trim() == ""){updateUser.setEmail(user.getEmail());}
            user.setEmail(updateUser.getEmail().trim());
            if(updateUser.getRole() == null){updateUser.setRole(user.getRole());}
            user.setRole(updateUser.getRole());
            return user;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User id " + id + " Does Not Exist !!!"));
        repository.saveAndFlush(editUser);
        return modelMapper.map(editUser, UserDTO.class);
    }

    public User saveNewUser(CreateUserDTO newUser) {
        newUser.setPassword(argonPassword(newUser.getPassword()));
        System.out.println(newUser.getPassword());
        User user = modelMapper.map(newUser, User.class);
        return repository.saveAndFlush(user);
    }

    public String argonPassword(String password){
        return argon2.hash(4, 1024 * 1024, 8, password);
    }

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private MatchService matchService;

    public ResponseEntity MatchUserDTO(@Valid MatchUserDTO matchUserDTO, HttpServletResponse httpServletResponse, ServletWebRequest request) throws Exception {
        Map<String,String> errorMap = new HashMap<>();
        String status;

        if (repository.existsUserByEmail(matchUserDTO.getEmail())) {
            User user = repository.findByEmail(matchUserDTO.getEmail());
            if (argon2.verify(matchUserDTO.getPassword(), user.getPassword())) {
                authenticate(matchUserDTO.getEmail(), matchUserDTO.getPassword());
                authenticate(matchUserDTO.getEmail(), matchUserDTO.getPassword());

                final UserDetails userDetails = matchService
                        .loadUserByUsername(matchUserDTO.getEmail());

                final String token = jwtTokenUtil.generateToken(userDetails);

                final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);
                return ResponseEntity.ok(new JwtResponse("Login Successful","Password Matched", token, refreshToken));
//                throw new ResponseStatusException(HttpStatus.OK, "Password Matched");
            } else {
                errorMap.put("message","Password NOT Matched");
                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                status = HttpStatus.UNAUTHORIZED.toString();
//            }
            }
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A user with the specified email DOES NOT exist");
        }

        HandleValidationErrors errors = new HandleValidationErrors(
                Date.from(Instant.now()),
                httpServletResponse.getStatus(),
                status,
                errorMap.get("message"),
                request.getRequest().getRequestURI());
        return ResponseEntity.status(httpServletResponse.getStatus()).body(errors);

    }

    private void authenticate(String email, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
