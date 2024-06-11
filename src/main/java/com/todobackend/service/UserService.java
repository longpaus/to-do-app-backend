package com.todobackend.service;


import com.todobackend.dto.UserDTO;
import com.todobackend.mapper.IUserMapper;
import com.todobackend.model.User;
import com.todobackend.repository.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {

    IUserRepository userRepository;
    IUserMapper userMapper;


    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = userMapper.fromDTO(userDTO);
        User createdUser = userRepository.save(user);
        return userMapper.toDTO(createdUser);
    }

    @Override
    public Optional<UserDTO> getUserById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("id not found"));
        return Optional.of(userMapper.toDTO(user));
    }

    @Override
    public User getUserbyUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElse(null);
    }

    @Override
    public Optional<UserDTO> updateUser(long id, User user) {
        return Optional.empty();
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
