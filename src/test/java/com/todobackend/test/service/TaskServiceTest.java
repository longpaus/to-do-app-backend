package com.todobackend.test.service;

import com.todobackend.dto.CreateTaskDTO;
import com.todobackend.dto.TaskDTO;
import com.todobackend.exception.IdNotFoundException;
import com.todobackend.mapper.ITaskMapper;
import com.todobackend.model.Task;
import com.todobackend.model.User;
import com.todobackend.repository.ITaskRepository;
import com.todobackend.repository.IUserRepository;
import com.todobackend.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private ITaskRepository taskRepository;

    @Mock
    private ITaskMapper taskMapper;

    @Mock
    private IUserRepository userRepository;

    private User user;
    private Task task;
    private TaskDTO taskDTO;
    private Date creationDate;

    @BeforeEach
    public void setUp() {
        creationDate = new Date(124, Calendar.JUNE, 17);
        user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setJoinedOn(creationDate);
        user.setPassword("password");

        task = new Task();
        task.setId(1L);
        task.setUser(user);
        task.setName("task1");
        task.setDueDate(creationDate);
        task.setCompleted(true);

        taskDTO = new TaskDTO();
        taskDTO.setId(task.getId());
        taskDTO.setName(task.getName());
        taskDTO.setDueDate("17.06.2024");
        taskDTO.setCompleted(task.isCompleted());
    }

    @Test
    public void testFindTaskById() {
        // Arrange
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskMapper.taskToTaskDTO(task)).thenReturn(taskDTO);

        // Act
        TaskDTO resultDTO = taskService.findTaskById(task.getId());

        // Assert
        assertNotNull(resultDTO);
        assertEquals(taskDTO.getId(), resultDTO.getId());
        assertEquals(taskDTO.getName(), resultDTO.getName());
        assertEquals(taskDTO.getDueDate(), resultDTO.getDueDate());
        assertEquals(taskDTO.isCompleted(), resultDTO.isCompleted());
        verify(taskRepository, times(1)).findById(task.getId());
        verify(taskMapper, times(1)).taskToTaskDTO(task);
    }

    @Test
    public void testFindTaskById_idNotFound() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IdNotFoundException ex = assertThrows(IdNotFoundException.class, () -> taskService.findTaskById(1L));
        assertEquals("Task Id not found", ex.getMessage());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskMapper, times(0)).taskToTaskDTO(any());
    }

    @Test
    public void testCreateTask() {
        // Arrange
        CreateTaskDTO createTaskDTO = new CreateTaskDTO();
        createTaskDTO.setUserId(task.getUser().getId());
        createTaskDTO.setName(task.getName());
        createTaskDTO.setDueDate("17.06.2024");

        when(userRepository.findById(createTaskDTO.getUserId())).thenReturn(Optional.of(user));
        when(taskMapper.createTaskDTOtoTask(createTaskDTO, user)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.taskToTaskDTO(task)).thenReturn(taskDTO);

        // Act
        TaskDTO resTaskDTO = taskService.createTask(createTaskDTO);

        // Assert
        assertNotNull(resTaskDTO);
        assertEquals(taskDTO.getId(), resTaskDTO.getId());
        assertEquals(taskDTO.getName(), resTaskDTO.getName());
        assertEquals(taskDTO.getDueDate(), resTaskDTO.getDueDate());
        assertEquals(taskDTO.isCompleted(), resTaskDTO.isCompleted());
        verify(userRepository, times(1)).findById(createTaskDTO.getUserId());
        verify(taskMapper, times(1)).createTaskDTOtoTask(createTaskDTO, user);
        verify(taskRepository, times(1)).save(task);
        verify(taskMapper, times(1)).taskToTaskDTO(task);
    }

    @Test
    public void testCreateTask_UserNotFound() {
        // Arrange
        CreateTaskDTO createTaskDTO = new CreateTaskDTO();
        createTaskDTO.setUserId(3L);
        createTaskDTO.setName("task");
        createTaskDTO.setDueDate("17.06.2024");
        when(userRepository.findById(3L)).thenReturn(Optional.empty());

        // Act & Assert
        IdNotFoundException ex = assertThrows(IdNotFoundException.class, () -> taskService.createTask(createTaskDTO));
        assertEquals("User Id not found", ex.getMessage());
        verify(userRepository, times(1)).findById(3L);
    }

    @Test
    public void testUpdateTask() {
        // Arrange
        taskDTO.setName("updatedTask");
        Task updatedTask = new Task();
        updatedTask.setId(task.getId());
        updatedTask.setName(taskDTO.getName());
        updatedTask.setDueDate(creationDate);
        updatedTask.setCompleted(taskDTO.isCompleted());
        updatedTask.setUser(user);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(userRepository.findById(taskDTO.getUserId())).thenReturn(Optional.of(user));
        when(taskMapper.taskDTOToTask(taskDTO, user)).thenReturn(updatedTask);
        when(taskRepository.save(updatedTask)).thenReturn(updatedTask);
        when(taskMapper.taskToTaskDTO(updatedTask)).thenReturn(taskDTO);

        // Act
        TaskDTO resultDTO = taskService.updateTask(task.getId(), taskDTO);

        // Assert
        assertNotNull(resultDTO);
        assertEquals(taskDTO.getId(), resultDTO.getId());
        assertEquals(taskDTO.getName(), resultDTO.getName());
        assertEquals(taskDTO.getDueDate(), resultDTO.getDueDate());
        assertEquals(taskDTO.isCompleted(), resultDTO.isCompleted());
        verify(taskRepository, times(1)).findById(task.getId());
        verify(userRepository, times(1)).findById(taskDTO.getUserId());
        verify(taskMapper, times(1)).taskDTOToTask(taskDTO, user);
        verify(taskRepository, times(1)).save(updatedTask);
        verify(taskMapper, times(1)).taskToTaskDTO(updatedTask);
    }

    @Test
    public void testUpdateTask_TaskNotFound() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IdNotFoundException ex = assertThrows(IdNotFoundException.class, () -> taskService.updateTask(1L, taskDTO));
        assertEquals("Task id not found", ex.getMessage());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdateTask_UserNotFound() {
        // Arrange
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(userRepository.findById(taskDTO.getUserId())).thenReturn(Optional.empty());

        // Act & Assert
        IdNotFoundException ex = assertThrows(IdNotFoundException.class, () -> taskService.updateTask(task.getId(), taskDTO));
        assertEquals("User not found", ex.getMessage());
        verify(taskRepository, times(1)).findById(task.getId());
        verify(userRepository, times(1)).findById(taskDTO.getUserId());
    }

    @Test
    public void testGetTasksByUserId() {
        // Arrange
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);

        when(taskRepository.getTasksByUserId(user.getId())).thenReturn(tasks);
        when(taskMapper.taskToTaskDTO(task)).thenReturn(taskDTO);

        // Act
        List<TaskDTO> resultDTOs = taskService.getTasksByUserId(user.getId());

        // Assert
        assertNotNull(resultDTOs);
        assertEquals(1, resultDTOs.size());
        TaskDTO resultDTO = resultDTOs.get(0);
        assertEquals(taskDTO.getId(), resultDTO.getId());
        assertEquals(taskDTO.getName(), resultDTO.getName());
        assertEquals(taskDTO.getDueDate(), resultDTO.getDueDate());
        assertEquals(taskDTO.isCompleted(), resultDTO.isCompleted());
        verify(taskRepository, times(1)).getTasksByUserId(user.getId());
        verify(taskMapper, times(1)).taskToTaskDTO(task);
    }
}
