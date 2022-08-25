package sit.int221.clinicservice.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.BeanDefinitionDsl;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.clinicservice.Role;
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

//    public EditUserDTO updateUser(@Valid EditUserDTO updateUser, Integer id) {
//        User user = repository.findById(id).map(o->mapUser(o, updateUser)).orElseThrow(()->
//                new ResponseStatusException(HttpStatus.BAD_REQUEST, "ไม่มี id เบอร์: "+ id));
//        repository.saveAndFlush(user);
//        return modelMapper.map(user, EditUserDTO.class);
//    }
//
//    private User mapUser(User existingUser, EditUserDTO editUserDTO){
//        if(editUserDTO.getName() != null){
//            existingUser.setName(editUserDTO.getName().trim());
//        }
//        if(editUserDTO.getEmail() != null){
//            existingUser.setEmail(editUserDTO.getEmail());
//        }
//        if (editUserDTO.getRole() != null){
//            existingUser.setRole(editUserDTO.getRole());
//        }
//        return existingUser;
//    }
}
