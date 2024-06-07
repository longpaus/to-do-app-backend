package com.todobackend.service;

import com.todobackend.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface IUserService {
    User createUser(User user);
    User getUserById(long id);
    User getUserbyUsername(String username);
    User updateUser(User user);
    void deleteUser(long id);

}
