package sit.int221.clinicservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.clinicservice.dtos.CreateUserDTO;
import sit.int221.clinicservice.dtos.EditUserDTO;
import sit.int221.clinicservice.dtos.UserDTO;
import sit.int221.clinicservice.entities.User;
import sit.int221.clinicservice.repositories.UserRepository;
import sit.int221.clinicservice.services.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @GetMapping("")
    public List<UserDTO> getAllUser(){
        return userService.getAllUser();
    }

    @GetMapping("{id}")
    public UserDTO getUserById(@PathVariable Integer id){
        return userService.getUserById(id);
    }

//create
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Validated @RequestBody CreateUserDTO createUserDTO){
        createUserDTO.setName(createUserDTO.getName().trim());
       return userService.saveNewUser (createUserDTO);
    }

//delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        userRepository.findById(id).orElseThrow(()->
            new ResponseStatusException(HttpStatus.NOT_FOUND,
                    id + " does not exist !!!"));
        userRepository.deleteById(id);
    }

//edit patch
    @PatchMapping("/{id}")
    public UserDTO update(@Valid @RequestBody EditUserDTO editUserDTO, @PathVariable Integer id) {
//        User user = userRepository.findById(id)
//                .map(o -> mapUser(o, editUserDTO))
//                .orElseThrow(() ->
//                        new ResponseStatusException(HttpStatus.NOT_FOUND, "ไม่พบ id เบอร์ " + id
//                        ));
//        user.setName(editUserDTO.getName().trim());
//        return userRepository.saveAndFlush(user);
        return userService.updateUser(editUserDTO, id);
    }

//    private User mapUser(User existingUser, EditUserDTO updateUser){
//        existingUser.setName(updateUser.getName());
//        existingUser.setEmail(updateUser.getEmail());
//        existingUser.setRole(updateUser.getRole());
//        return existingUser;
//    }
}
