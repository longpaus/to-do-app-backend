package com.todobackend.service;

import com.todobackend.dto.UserDTO;
import com.todobackend.model.User;
import org.springframework.stereotype.Service;

@Service
public interface IUserService {
    UserDTO createUser(UserDTO user);
    UserDTO getUserById(long id);
    UserDTO getUserByUsername(String username);
    UserDTO updateUser(long id, User user);
    void deleteUser(long id);

}
