package com.todobackend.test.controller;

import com.todobackend.dto.UserDTO;
import com.todobackend.dto.UserLoginDTO;
import com.todobackend.exception.UserNameExistException;
import com.todobackend.model.User;
import com.todobackend.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IUserRepository userRepository;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();

        // Clear database before each test
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateUser() throws Exception {
        UserDTO newUserDTO = new UserDTO();
        newUserDTO.setUsername("newusername");
        newUserDTO.setPassword("newpassword");
        newUserDTO.setJoinedOn("01.07.2023");

        String userJson = objectMapper.writeValueAsString(newUserDTO);

        MvcResult res = mockMvc.perform(post("/user/create")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String responseString = res.getResponse().getContentAsString();
        UserDTO responseUserDTO = objectMapper.readValue(responseString, UserDTO.class);

        assertEquals(newUserDTO.getUsername(), responseUserDTO.getUsername());
        assertEquals(newUserDTO.getPassword(), responseUserDTO.getPassword());
        assertEquals("01.07.2023", responseUserDTO.getJoinedOn());
        
        // Verify that the user was persisted
        Optional<User> createdUser = userRepository.findByUsername("newusername");
        assertTrue(createdUser.isPresent());
        assertEquals("newusername", createdUser.get().getUsername());
        assertEquals("newpassword", createdUser.get().getPassword());
    }

    @Test
    @Description("create user: username already exist")
    public void createUserUsernameExist() throws Exception {
        // arrange
        Date creationDate = new Date(124, Calendar.JUNE, 17);
        User user = new User();
        user.setUsername("newusername");
        user.setPassword("newpassword");
        user.setJoinedOn(creationDate);
        userRepository.save(user);
        UserDTO newUserDTO = new UserDTO();
        newUserDTO.setUsername("newusername");
        newUserDTO.setPassword("newpassword");
        newUserDTO.setJoinedOn("01.07.2023");

        String userJson = objectMapper.writeValueAsString(newUserDTO);

        // Act and Assert
        mockMvc.perform(post("/user/create")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertInstanceOf(UserNameExistException.class, result.getResolvedException()))
                .andExpect(status().isConflict());
    }

    @Test
    public void loginTest() throws Exception {
        // arrange
        Date creationDate = new Date(124, Calendar.JUNE, 17);
        User user = new User();
        user.setUsername("newusername");
        user.setPassword("newpassword");
        user.setJoinedOn(creationDate);
        userRepository.save(user);

        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUserName("newusername");
        userLoginDTO.setPassword("newpassword");

        String loginJson = objectMapper.writeValueAsString(userLoginDTO);

        // act & assert
        MvcResult res = mockMvc.perform(post("/user/login")
                        .content(loginJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = res.getResponse().getContentAsString();
        UserDTO responseUserDTO = objectMapper.readValue(responseBody, UserDTO.class);
        assertEquals(responseUserDTO.getUsername(), user.getUsername());
        assertEquals(responseUserDTO.getPassword(), user.getPassword());
    }

}