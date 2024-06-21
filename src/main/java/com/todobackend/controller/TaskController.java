package com.todobackend.controller;

import com.todobackend.dto.CreateTaskDTO;
import com.todobackend.dto.TaskDTO;
import com.todobackend.model.Task;
import com.todobackend.service.TaskService;
import com.todobackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/task/create")
    ResponseEntity<TaskDTO> createTask(@RequestBody CreateTaskDTO createTaskDTO) {
        return new ResponseEntity<>(taskService.createTask(createTaskDTO),HttpStatus.CREATED);
    }

    @PostMapping("/task/update/{taskId}")
    ResponseEntity<TaskDTO> updateTask(@PathVariable long taskId, @RequestBody TaskDTO taskDTO) {
        TaskDTO updatedTask = taskService.updateTask(taskId, taskDTO);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @GetMapping("/tasks/{userId}")
    ResponseEntity<List<TaskDTO>> getAllTasks(@PathVariable long userId) {
        List<TaskDTO> tasksDTO = taskService.getTasksByUserId(userId);
        return new ResponseEntity<>(tasksDTO, HttpStatus.OK);
    }
}
