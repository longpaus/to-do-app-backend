package com.todobackend.service;

import com.todobackend.dto.UserDTO;
import com.todobackend.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface IUserService {
    UserDTO createUser(UserDTO user);
    Optional<UserDTO> getUserById(long id);
    Optional<UserDTO> getUserbyUsername(String username);
    Optional<UserDTO> updateUser(long id, User user);
    void deleteUser(long id);

}
