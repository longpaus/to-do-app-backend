package com.todobackend.test.repository;

import com.todobackend.model.Task;
import com.todobackend.model.User;
import com.todobackend.repository.ITaskRepository;
import com.todobackend.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@DataJpaTest
public class TaskRepositoryTest {

    @Autowired
    ITaskRepository taskRepository;

    @Autowired
    IUserRepository userRepository;


    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        taskRepository.deleteAll();
    }
    @Test
    public void testGetTasksByUserId() {
        // arrange
        Date creationDate = new Date(124, Calendar.JUNE, 17);
        User user1 = new User();
        user1.setUsername("username");
        user1.setJoinedOn(creationDate);
        user1.setPassword("password");
        User createdUser1 = userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("username2");
        user2.setJoinedOn(creationDate);
        user2.setPassword("password");
        User createdUser2 = userRepository.save(user2);

        Task user2Task = new Task();
        user2Task.setUser(createdUser2);
        user2Task.setName("task1");
        user2Task.setDueDate(null);
        user2Task.setCompleted(true);
        taskRepository.save(user2Task);

        Task task1 = new Task();
        task1.setUser(createdUser1);
        task1.setName("task1");
        task1.setDueDate(null);
        task1.setCompleted(true);
        Task createdTask1 = taskRepository.save(task1);

        Task task2 = new Task();
        task2.setUser(createdUser1);
        task2.setName("task2");
        task2.setDueDate(creationDate);
        task2.setCompleted(true);
        Task createdTask2 = taskRepository.save(task2);

        // act
        List<Task> tasks = taskRepository.getTasksByUserId(createdUser1.getId());

        // assert
        assertEquals(2, tasks.size());

        Task actualTask1 = tasks.getFirst();
        assertEquals(createdTask1.getName(), actualTask1.getName());
        assertNull(actualTask1.getDueDate());
        assertTrue(actualTask1.isCompleted());
        assertEquals(createdUser1, actualTask1.getUser());

        Task actualTask2 = tasks.get(1);
        assertEquals(createdTask2.getName(), actualTask2.getName());
        assertEquals(creationDate,actualTask2.getDueDate());
        assertTrue(actualTask2.isCompleted());
        assertEquals(createdUser1, actualTask2.getUser());
    }

    @Test
    public void testGetTaskById_userHaveNoTask() {
        // arrange
        Date creationDate = new Date(124, Calendar.JUNE, 17);
        User user1 = new User();
        user1.setUsername("username");
        user1.setJoinedOn(creationDate);
        user1.setPassword("password");
        User createdUser1 = userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("username2");
        user2.setJoinedOn(creationDate);
        user2.setPassword("password");
        User createdUser2 = userRepository.save(user2);

        Task user2Task = new Task();
        user2Task.setUser(createdUser2);
        user2Task.setName("task1");
        user2Task.setDueDate(null);
        user2Task.setCompleted(true);
        taskRepository.save(user2Task);

        // act
        List<Task> user1Tasks = taskRepository.getTasksByUserId(createdUser1.getId());
        List<Task> user2Tasks = taskRepository.getTasksByUserId(createdUser2.getId());
        // assert
        assertTrue(user1Tasks.isEmpty());

        assertEquals(1, user2Tasks.size());
    }

    @Test
    public void testGetTaskById_userDontExist() {
        List<Task> userTasks = taskRepository.getTasksByUserId(33L);
        assertTrue(userTasks.isEmpty());
    }
}
