package com.todobackend.test.controller;

import com.todobackend.dto.UserDTO;
import com.todobackend.dto.UserFormDTO;
import com.todobackend.exception.UserNameExistException;
import com.todobackend.exception.UserNameNotFoundException;
import com.todobackend.model.User;
import com.todobackend.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
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
@ActiveProfiles("test")
public class UserControllerIntegrationTest {
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
    void createUserTest() throws Exception {
        UserFormDTO userFormDTO = new UserFormDTO();
        userFormDTO.setUsername("newusername");
        userFormDTO.setPassword("password");


        String userJson = objectMapper.writeValueAsString(userFormDTO);

        MvcResult res = mockMvc.perform(post("/user/create")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String responseString = res.getResponse().getContentAsString();
        UserDTO responseUserDTO = objectMapper.readValue(responseString, UserDTO.class);

        assertEquals(userFormDTO.getUsername(), responseUserDTO.getUsername());
        assertEquals(userFormDTO.getPassword(), responseUserDTO.getPassword());
        assertNotNull(responseUserDTO.getJoinedOn());

        // Verify that the user was persisted
        Optional<User> createdUser = userRepository.findByUsername(userFormDTO.getUsername());
        assertTrue(createdUser.isPresent());
        assertEquals(userFormDTO.getUsername(), createdUser.get().getUsername());
        assertEquals(userFormDTO.getPassword(), createdUser.get().getPassword());
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

        UserFormDTO userFormDTO = new UserFormDTO();
        userFormDTO.setUsername("newusername");
        userFormDTO.setPassword("password");

        String userJson = objectMapper.writeValueAsString(userFormDTO);

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

        UserFormDTO userFormDTO = new UserFormDTO();
        userFormDTO.setUsername(user.getUsername());
        userFormDTO.setPassword(user.getPassword());


        String userJson = objectMapper.writeValueAsString(userFormDTO);

        // act & assert
        MvcResult res = mockMvc.perform(post("/user/login")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = res.getResponse().getContentAsString();
        UserDTO responseUserDTO = objectMapper.readValue(responseBody, UserDTO.class);
        assertEquals(responseUserDTO.getUsername(), user.getUsername());
        assertEquals(responseUserDTO.getPassword(), user.getPassword());
    }

    @Test
    public void loginUsernameDontExistTest() throws Exception {
        UserFormDTO userFormDTO = new UserFormDTO();
        userFormDTO.setUsername("newusername");
        userFormDTO.setPassword("password");


        String userJson = objectMapper.writeValueAsString(userFormDTO);

        mockMvc.perform(post("/user/login")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertInstanceOf(UserNameNotFoundException.class, result.getResolvedException()))
                .andExpect(status().isNotFound());

    }

    @Test
    public void loginPasswordDontMatchTest() throws Exception {
        // arrange
        Date creationDate = new Date(124, Calendar.JUNE, 17);
        User user = new User();
        user.setUsername("newusername");
        user.setPassword("newpassword");
        user.setJoinedOn(creationDate);
        userRepository.save(user);

        UserFormDTO userFormDTO = new UserFormDTO();
        userFormDTO.setUsername(user.getUsername());
        userFormDTO.setPassword("differentpassword");


        String userJson = objectMapper.writeValueAsString(userFormDTO);

        mockMvc.perform(post("/user/login")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

}