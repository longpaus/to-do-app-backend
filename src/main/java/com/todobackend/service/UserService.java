package com.todobackend.service;


import com.todobackend.dto.UserDTO;
import com.todobackend.dto.UserFormDTO;
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

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IUserMapper userMapper;

    public UserService(IUserMapper userMapper) {
        this.userMapper = userMapper;
    }


    public UserDTO createUser(UserFormDTO userFormDTO) throws UserNameExistException {
        Optional<User> existingUser = userRepository.findByUsername(userFormDTO.getUsername());
        if (existingUser.isPresent()) {
            throw new UserNameExistException("username already exist");
        }
        User user = userMapper.userFormDTOToUser(userFormDTO);
        User createdUser = userRepository.save(user);
        return userMapper.userToUserDTO(createdUser);
    }

    @Override
    public UserDTO getUserById(long id) throws IdNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException("id not found"));
        return userMapper.userToUserDTO(user);
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNameNotFoundException("username not found"));
        return userMapper.userToUserDTO(user);
    }

    @Override
    public UserDTO updateUser(long id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException("id not found"));

        User savedUser = userRepository.save(existingUser);
        return userMapper.userToUserDTO(savedUser);
    }


    @Override
    public void deleteUser(long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException("id not found"));
        userRepository.deleteById(id);
    }
}
