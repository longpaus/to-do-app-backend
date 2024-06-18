//package com.todobackend.test;
//
//import com.todobackend.controller.UserController;
//import com.todobackend.dto.UserDTO;
//import com.todobackend.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MockMvcBuilder;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.Date;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(TestUserController.class)
//public class TestUserController {
//    @Mock
//    private MockMvc mvc;
//
//    @InjectMocks
//    private UserController userController;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserService userService;
//
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
//    }
//
//    @Test
//    public void testCreateUser() throws Exception {
//        UserDTO userDTO = new UserDTO();
//        userDTO.setId(1);
//        userDTO.setUsername("testuser");
//        userDTO.setPassword("password");
//        userDTO.setJoinedOn(new Date().toString());
//
//
//        when(userService.createUser(any(UserDTO.class))).thenReturn(userDTO);
//
//        mockMvc.perform(post("/user/create")
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.username").value("testuser"))
//                .andExpect(jsonPath("$.password").value("password"));
//    }
//
//}
