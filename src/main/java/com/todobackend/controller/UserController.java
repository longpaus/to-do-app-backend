package com.todobackend.controller;

import com.todobackend.dto.UserDTO;
import com.todobackend.dto.UserFormDTO;
import com.todobackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String hello() {
        return "Hello, World";
    }

    @PostMapping("/user/create")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserFormDTO newUserDTO) {
        UserDTO createdUserDTO = userService.createUser(newUserDTO);
        return new ResponseEntity<>(createdUserDTO, HttpStatus.CREATED);
    }

    @PostMapping("/user/login")
    public ResponseEntity<UserDTO> login(@RequestBody UserFormDTO userFormDTO) {

        UserDTO userDTO = userService.getUserByUsername(userFormDTO.getUsername());

        if (userDTO.getPassword().equals(userFormDTO.getPassword())) {
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }
}
