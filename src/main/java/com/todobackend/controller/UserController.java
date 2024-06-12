package com.todobackend.controller;

import com.todobackend.dto.UserDTO;
import com.todobackend.dto.UserLoginDTO;
import com.todobackend.model.User;
import com.todobackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Optional;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String hello() {
        return "hello, world!!";
    }

    @PostMapping("/user/create")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO newuserDTO) {
        UserDTO createdUserDTO = userService.createUser(newuserDTO);
        return new ResponseEntity<>(createdUserDTO, HttpStatus.CREATED);
    }

    @PostMapping("/user/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDTO userLoginDTO) {

        UserDTO user = userService.getUserbyUsername(userLoginDTO.getUserName());
        if (user.getPassword().equals(userLoginDTO.getPassword())) {
            return new ResponseEntity<>(user.getUsername(), HttpStatus.OK);
        }

        return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
    }
}
