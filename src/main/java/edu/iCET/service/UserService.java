package edu.iCET.service;

import edu.iCET.model.dto.UserDTO;
import edu.iCET.model.entity.User;
import edu.iCET.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<UserDTO> getAllUsers() {
        List<User> userList = userRepository.findAll();

        List<UserDTO> userDTOS = new ArrayList<>();

        for (User user : userList){
            userDTOS.add(new UserDTO(
                    user.getId(),
                    user.getName(),
                    user.getTier(),
                    user.getEmail()
            ));
        }
        return userDTOS;
    }

    public UserDTO getUserById(Long id) {
        User byId = userRepository.getById(id);

        return new UserDTO(
                byId.getId(),
                byId.getName(),
                byId.getTier(),
                byId.getEmail()
        );
    }

    public void saveUser(User user) {
        User user1 = new User(
                user.getId(),
                user.getName(),
                user.getTier(),
                user.getEmail()
        );

        userRepository.save(user1);
    }


}
