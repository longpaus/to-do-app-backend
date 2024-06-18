package com.todobackend.service;


import com.todobackend.dto.UserDTO;
import com.todobackend.exception.IdNotFoundException;
import com.todobackend.exception.UserNameExistException;
import com.todobackend.exception.UserNameNotFoundException;
import com.todobackend.mapper.IUserMapper;
import com.todobackend.model.User;
import com.todobackend.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {

    IUserRepository userRepository;

    IUserMapper userMapper;

    @Autowired
    public UserService(IUserRepository userRepository, IUserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDTO createUser(UserDTO userDTO) {
        Optional<User> existingUser = userRepository.findByUsername(userDTO.getUsername());
        if(existingUser.isPresent()) {
            throw new UserNameExistException("username already exist");
        }
        User user = userMapper.fromDTO(userDTO);
        User createdUser = userRepository.save(user);
        return userMapper.toDTO(createdUser);
    }

    @Override
    public UserDTO getUserById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException("id not found"));
        return userMapper.toDTO(user);
    }

    @Override
    public UserDTO getUserByUsername(String username) {
       User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new UserNameNotFoundException("username not found"));
        return userMapper.toDTO(user);
    }

    @Override
    public UserDTO updateUser(long id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException("id not found"));

        User savedUser = userRepository.save(existingUser);
        return userMapper.toDTO(savedUser);
    }


    @Override
    public void deleteUser(long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException("id not found"));
        userRepository.deleteById(id);
    }
}
