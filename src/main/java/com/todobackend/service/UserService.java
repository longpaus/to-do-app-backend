package com.todobackend.service;


import com.todobackend.model.User;
import com.todobackend.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {

    IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    @Override
    public User getUserbyUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElse(null);
    }


    @Override
    public User updateUser( User user) {
        // Check if the user exists
        Optional<User> optionalExistingUser = userRepository.findById(user.getId());
        if (optionalExistingUser.isPresent()) {
            User existingUser = optionalExistingUser.get();
            // Update user properties
            existingUser.setUsername(user.getUsername());
            existingUser.setPassword(user.getPassword());
            // Save the updated user using UserRepository
            return userRepository.save(existingUser);
        } else {
            // User with the given id not found
            return null;
        }
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }
}
