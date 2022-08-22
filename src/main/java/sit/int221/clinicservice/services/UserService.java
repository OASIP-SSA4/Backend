package sit.int221.clinicservice.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.clinicservice.dtos.CreateUserDTO;
import sit.int221.clinicservice.dtos.EditUserDTO;
import sit.int221.clinicservice.dtos.UserDTO;
import sit.int221.clinicservice.entities.User;
import sit.int221.clinicservice.repositories.UserRepository;

import javax.validation.Valid;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;

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

    public UserDTO editUser(@Valid EditUserDTO editUserDTO, Integer id) {
        checkConstraintEmailUser(editUserDTO.getEmail(),id);
        checkConstraintNameUser(editUserDTO.getName(),id);
        User editUser = repository.findById(id).map(user -> {
            if(editUserDTO.getName() == null || editUserDTO.getName().trim() == ""){editUserDTO.setName(user.getName());}
            user.setName(editUserDTO.getName().trim());
            if(editUserDTO.getEmail() == null || editUserDTO.getEmail().trim() == ""){editUserDTO.setEmail(user.getEmail());}
            user.setEmail(editUserDTO.getEmail().trim());
            if(editUserDTO.getRole() == null){editUserDTO.setRole(user.getRole());}
            user.setRole(editUserDTO.getRole());
            return user;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User id " + id + " Does Not Exist !!!"));
        repository.saveAndFlush(editUser);
        return modelMapper.map(editUser, UserDTO.class);
    }
}
