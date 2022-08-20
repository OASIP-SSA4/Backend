package sit.int221.clinicservice.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.clinicservice.dtos.CreateUserDTO;
import sit.int221.clinicservice.dtos.UserDTO;
import sit.int221.clinicservice.entities.User;
import sit.int221.clinicservice.repositories.UserRepository;

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
        createUserDTO.setName(createUserDTO.getName().trim());
        createUserDTO.setEmail(createUserDTO.getEmail().trim());
        User user = modelMapper.map(createUserDTO, User.class);
        return repository.saveAndFlush(user);
    }

    public List<UserDTO> getAllUser(){
        List<User> userList = repository.findAll();
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
}
