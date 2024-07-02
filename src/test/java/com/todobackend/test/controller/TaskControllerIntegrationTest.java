package com.todobackend.test.controller;

import com.todobackend.dto.CreateTaskDTO;
import com.todobackend.dto.TaskDTO;
import com.todobackend.dto.TaskGroupsDTO;
import com.todobackend.exception.IdNotFoundException;
import com.todobackend.model.Task;
import com.todobackend.model.User;
import com.todobackend.repository.ITaskRepository;
import com.todobackend.repository.IUserRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static  org. springframework. test. web. servlet. request. MockMvcRequestBuilders.get;
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
        userRepository.deleteAll();
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

        MvcResult res = mockMvc.perform(post("/tasks/create")
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

    @Test
    public void testGetGroupTasks() throws Exception{
        Date creationDate = new Date(124, Calendar.JUNE, 17);
        User user = new User();
        user.setUsername("username");
        user.setJoinedOn(creationDate);
        user.setPassword("password");
        User savedUser = userRepository.save(user);



        LocalDate localDateToday = LocalDate.now();
        Date today = Date.from(localDateToday.plusDays(0).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date tomorrow = Date.from(localDateToday.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date next7Days = Date.from(localDateToday.plusDays(6).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date overdue =  Date.from(localDateToday.plusDays(-6).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date later =  Date.from(localDateToday.plusDays(9).atStartOfDay(ZoneId.systemDefault()).toInstant());

        Task task1 = new Task();
        task1.setName("task 1");
        task1.setDueDate(today);
        task1.setUser(savedUser);
        task1.setCompleted(false);
        Task savedTask1 = taskRepository.save(task1);

        Task task2 = new Task();
        task2.setName("task 2");
        task2.setDueDate(next7Days);
        task2.setUser(savedUser);
        task2.setCompleted(false);
        Task savedTask2 = taskRepository.save(task2);

        Task task3 = new Task();
        task3.setName("task 3");
        task3.setDueDate(tomorrow);
        task3.setUser(savedUser);
        task3.setCompleted(true);
        Task savedTask3 = taskRepository.save(task3);

        Task task4 = new Task();
        task4.setName("task 4");
        task4.setDueDate(null);
        task4.setUser(savedUser);
        task4.setCompleted(false);
        Task savedTask4 = taskRepository.save(task4);

        Task task5 = new Task();
        task5.setName("task 5");
        task5.setDueDate(overdue);
        task5.setUser(savedUser);
        task5.setCompleted(false);
        Task savedTask5 = taskRepository.save(task5);

        Task task6 = new Task();
        task6.setName("task 6");
        task6.setDueDate(later);
        task6.setUser(savedUser);
        task6.setCompleted(false);
        Task savedTask6 = taskRepository.save(task6);

        Task task7 = new Task();
        task7.setName("task 7");
        task7.setDueDate(tomorrow);
        task7.setUser(savedUser);
        task7.setCompleted(false);
        Task savedTask7 = taskRepository.save(task7);

        MvcResult mvcResult = mockMvc.perform(get("/tasks/grouped")
                        .header("userId", savedUser.getId())
                        .param("groupBy", "date")
                        .param("sortBy", "date")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();
        TaskGroupsDTO responseTasksDTO = objectMapper.readValue(responseString, TaskGroupsDTO.class);

        assertEquals(1, responseTasksDTO.getGroups().get("Today").size());
        assertEquals(1,responseTasksDTO.getGroups().get("Later").size());
        assertEquals(1,responseTasksDTO.getGroups().get("No Date").size());
        assertEquals(1,responseTasksDTO.getGroups().get("Next 7 Days").size());
        assertEquals(1,responseTasksDTO.getGroups().get("Completed").size());
        assertEquals(1,responseTasksDTO.getGroups().get("Overdue").size());
        assertEquals(1, responseTasksDTO.getGroups().get("Tomorrow").size());
    }
}
