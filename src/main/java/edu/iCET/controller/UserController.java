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


}
