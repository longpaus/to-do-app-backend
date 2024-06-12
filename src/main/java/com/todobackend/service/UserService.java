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


    public UserDTO createUser(UserDTO userDTO) {
        Optional<User> existingUser = userRepository.findByUsername(userDTO.getUsername());
        if(existingUser.isPresent()) {
            throw new RuntimeException("username already exist");
        }
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
    public Optional<UserDTO> getUserbyUsername(String username) {
       User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("username not found"));
        return Optional.of(userMapper.toDTO(user));
    }

    @Override
    public Optional<UserDTO> updateUser(long id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("id not found"));

        User savedUser = userRepository.save(existingUser);
        return Optional.of(userMapper.toDTO(savedUser));
    }


    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }
}
