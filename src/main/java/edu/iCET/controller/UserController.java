package edu.iCET.controller;

import edu.iCET.model.dto.UserDTO;
import edu.iCET.model.entity.User;
import edu.iCET.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public String loadUser(){

        return "load user controller...";
    }

    @GetMapping("/getAllUsers")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/save")
    public void saveUser(@RequestBody User user) {
        userService.saveUser(user);
    }

    @PutMapping("/update")
    public void update(@RequestBody UserDTO userDTO){
        userService.update(userDTO);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "User deleted successfully!";
    }
}
