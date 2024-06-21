package com.todobackend.test.mapper;

import com.todobackend.dto.CreateTaskDTO;
import com.todobackend.dto.TaskDTO;
import com.todobackend.mapper.ITaskMapper;
import com.todobackend.model.Task;
import com.todobackend.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class TaskMapperTest {
    @Autowired
    private ITaskMapper taskMapper;

    @Test
    public void testTaskToTaskDTO() {
        Date creationDate = new Date(124, Calendar.JUNE, 17);
        User user = new User();
        user.setId(1);
        user.setUsername("userName");
        user.setPassword("password");
        user.setJoinedOn(creationDate);

        Task task = new Task();
        task.setName("todo");
        task.setCompleted(false);
        task.setDueDate(creationDate);
        task.setUser(user);

        TaskDTO taskDTO = taskMapper.taskToTaskDTO(task);
        assertNotNull(taskDTO);
        assertEquals(task.getName(), taskDTO.getName());
        assertEquals(task.isCompleted(), taskDTO.isCompleted());
        assertEquals("17.06.2024", taskDTO.getDueDate());
        assertEquals(user.getId(), taskDTO.getUserId());
    }

    @Test
    public void testTaskDTOToTask() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setName("todo");
        taskDTO.setCompleted(false);
        taskDTO.setDueDate("17.06.2024");
        taskDTO.setUserId(1L);
        taskDTO.setId(1L);

        Date creationDate = new Date(124, Calendar.JUNE, 17);
        User user = new User();
        user.setId(1L);
        user.setUsername("userName");
        user.setPassword("password");
        user.setJoinedOn(creationDate);

        Task task = taskMapper.taskDTOToTask(taskDTO, user);
        assertNotNull(task);
        assertEquals(taskDTO.getName(), task.getName());
        assertEquals(task.isCompleted(), taskDTO.isCompleted());
        assertEquals(creationDate, task.getDueDate());
        assertEquals(user, task.getUser());
        assertEquals(taskDTO.getId(), task.getId());;
    }

    @Test
    public void testCreateTaskDTO_toTask() {
        CreateTaskDTO createTaskDTO = new CreateTaskDTO();
        createTaskDTO.setName("new task");
        createTaskDTO.setDueDate("17.06.2024");
        createTaskDTO.setUserId(1L);

        Date creationDate = new Date(124, Calendar.JUNE, 17);
        User user = new User();
        user.setId(1L);
        user.setUsername("userName");
        user.setPassword("password");
        user.setJoinedOn(creationDate);

        Task task = taskMapper.createTaskDTOtoTask(createTaskDTO, user);
        assertNotNull(task);
        assertEquals(createTaskDTO.getName(), task.getName());
        assertEquals(1, task.getId()); // default value of taskMapper
        assertEquals(creationDate, task.getDueDate());
        assertEquals(user, task.getUser());
        assertFalse(task.isCompleted());
    }
}
