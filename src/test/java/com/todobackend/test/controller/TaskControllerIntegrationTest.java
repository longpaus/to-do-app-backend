package com.todobackend.test.controller;

import com.todobackend.dto.CreateTaskDTO;
import com.todobackend.dto.TaskDTO;
import com.todobackend.dto.UserDTO;
import com.todobackend.exception.IdNotFoundException;
import com.todobackend.model.Task;
import com.todobackend.model.User;
import com.todobackend.repository.ITaskRepository;
import com.todobackend.repository.IUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.text.html.Option;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TaskControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ITaskRepository taskRepository;

    @Autowired
    private IUserRepository userRepository;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();

        taskRepository.deleteAll();
    }

    @Test
    void testCreateTask() throws Exception {
        Date creationDate = new Date(124, Calendar.JUNE, 17);
        User user = new User();
        user.setUsername("username");
        user.setJoinedOn(creationDate);
        user.setPassword("password");
        User savedUser = userRepository.save(user);

        CreateTaskDTO createTaskDTO = new CreateTaskDTO();
        createTaskDTO.setName("task");
        createTaskDTO.setDueDate("17.06.2024");
        createTaskDTO.setUserId(savedUser.getId());

        TaskDTO expectedTaskDTO = new TaskDTO();
        expectedTaskDTO.setName(createTaskDTO.getName());
        expectedTaskDTO.setDueDate(createTaskDTO.getDueDate());
        expectedTaskDTO.setUserId(savedUser.getId());
        expectedTaskDTO.setCompleted(false);

        String taskJson = objectMapper.writeValueAsString(createTaskDTO);

        MvcResult res = mockMvc.perform(post("/task/create")
                .content(taskJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String responseString = res.getResponse().getContentAsString();
        TaskDTO responseTaskDTO = objectMapper.readValue(responseString, TaskDTO.class);

        // verify the response dto is correct
        assertEquals(expectedTaskDTO.getName(), responseTaskDTO.getName());
        assertEquals(expectedTaskDTO.getDueDate(), responseTaskDTO.getDueDate());
        assertEquals(expectedTaskDTO.getUserId(), responseTaskDTO.getUserId());
        assertFalse(responseTaskDTO.isCompleted());

        // verify that the task is persisted
        Optional<Task> optionalCreatedTask = taskRepository.findById(responseTaskDTO.getId());
        assertTrue(optionalCreatedTask.isPresent());
        Task createdTask = optionalCreatedTask.get();
        assertEquals(expectedTaskDTO.getName(),createdTask.getName());
        assertEquals(creationDate, createdTask.getDueDate());
        assertEquals(savedUser.getId(), createdTask.getUser().getId());
        assertFalse(createdTask.isCompleted());
    }

    @Test
    public void testCreateTask_userNotFound() throws Exception {
        CreateTaskDTO createTaskDTO = new CreateTaskDTO();
        createTaskDTO.setName("task");
        createTaskDTO.setDueDate("17.06.2024");
        createTaskDTO.setUserId(3L);

        String taskJson = objectMapper.writeValueAsString(createTaskDTO);

        mockMvc.perform(post("/tasks/create")
                        .content(taskJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertInstanceOf(IdNotFoundException.class, result.getResolvedException()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateTask() throws Exception {
        Date updatedDate = new Date(125, Calendar.JUNE, 17);
        Date creationDate = new Date(124, Calendar.JUNE, 17);
        User user = new User();
        user.setUsername("username");
        user.setJoinedOn(creationDate);
        user.setPassword("password");
        User savedUser = userRepository.save(user);

        Task task = new Task();
        task.setName("task");
        task.setDueDate(creationDate);
        task.setUser(savedUser);
        task.setCompleted(true);
        Task savedTask = taskRepository.save(task);


        TaskDTO updatedTaskDTO = new TaskDTO();
        updatedTaskDTO.setId(savedTask.getId());
        updatedTaskDTO.setName("change task name");
        updatedTaskDTO.setDueDate("17.06.2025");
        updatedTaskDTO.setUserId(savedUser.getId());
        updatedTaskDTO.setCompleted(false);

        String taskJson = objectMapper.writeValueAsString(updatedTaskDTO);

        MvcResult res = mockMvc.perform(put("/tasks/update/"+savedTask.getId())
                        .content(taskJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = res.getResponse().getContentAsString();
        TaskDTO responseTaskDTO = objectMapper.readValue(responseString, TaskDTO.class);

        // verify that response dto is correct
        assertEquals(updatedTaskDTO, responseTaskDTO);


        // verify the data persisted
        Optional<Task> optionalUpdatedTask = taskRepository.findById(responseTaskDTO.getId());

        assertTrue(optionalUpdatedTask.isPresent());

        Task updatedTask = optionalUpdatedTask.get();

        assertEquals(updatedTaskDTO.getName(), updatedTask.getName());
        assertEquals(updatedDate, updatedTask.getDueDate());
        assertEquals(updatedTaskDTO.getUserId(), updatedTask.getUser().getId());
        assertEquals(updatedTaskDTO.isCompleted(), updatedTask.isCompleted());
        assertEquals(updatedTaskDTO.getId(), updatedTask.getId());
    }

    @Test
    public void testUpdateTask_userNotFound() throws Exception {
        Date creationDate = new Date(124, Calendar.JUNE, 17);
        User user = new User();
        user.setUsername("username");
        user.setJoinedOn(creationDate);
        user.setPassword("password");
        User savedUser = userRepository.save(user);

        Task task = new Task();
        task.setName("task");
        task.setDueDate(creationDate);
        task.setUser(savedUser);
        task.setCompleted(true);
        Task savedTask = taskRepository.save(task);


        TaskDTO updatedTaskDTO = new TaskDTO();
        updatedTaskDTO.setId(savedTask.getId());
        updatedTaskDTO.setName("change task name");
        updatedTaskDTO.setDueDate("17.06.2025");
        updatedTaskDTO.setUserId(4L);
        updatedTaskDTO.setCompleted(false);

        String taskJson = objectMapper.writeValueAsString(updatedTaskDTO);

        mockMvc.perform(put("/tasks/update/"+updatedTaskDTO.getId())
                        .content(taskJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertInstanceOf(IdNotFoundException.class, result.getResolvedException()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateTask_taskIdNotFound() throws Exception {
        Date creationDate = new Date(124, Calendar.JUNE, 17);
        User user = new User();
        user.setUsername("username");
        user.setJoinedOn(creationDate);
        user.setPassword("password");
        User savedUser = userRepository.save(user);

        Task task = new Task();
        task.setName("task");
        task.setDueDate(creationDate);
        task.setUser(savedUser);
        task.setCompleted(true);
        Task savedTask = taskRepository.save(task);


        TaskDTO updatedTaskDTO = new TaskDTO();
        updatedTaskDTO.setId(4L);
        updatedTaskDTO.setName("change task name");
        updatedTaskDTO.setDueDate("17.06.2025");
        updatedTaskDTO.setUserId(savedUser.getId());
        updatedTaskDTO.setCompleted(false);

        String taskJson = objectMapper.writeValueAsString(updatedTaskDTO);

        mockMvc.perform(put("/tasks/update/"+updatedTaskDTO.getId())
                        .content(taskJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertInstanceOf(IdNotFoundException.class, result.getResolvedException()))
                .andExpect(status().isNotFound());
    }
}
