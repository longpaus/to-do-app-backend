package com.todobackend.controller;

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
    ResponseEntity<TaskDTO> createTask( @RequestBody TaskDTO taskDTO) {
        return new ResponseEntity<>(taskService.createTask(taskDTO),HttpStatus.CREATED);
    }

    @PostMapping("/task/update/{taskId}")
    ResponseEntity<TaskDTO> updateTask(@PathVariable long taskId, @RequestBody TaskDTO taskDTO) {
        Optional<TaskDTO> optionalTaskDTO = taskService.updateTask(taskId, taskDTO);

        return optionalTaskDTO
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @GetMapping("/tasks/{userName}")
    ResponseEntity<List<TaskDTO>> getAllTasks(@PathVariable String userName) {
        Optional<List<TaskDTO>> optionalTasksDTO = taskService.getTasksByUserName(userName);
        return optionalTasksDTO
                .map(taskDTOS -> new ResponseEntity<>(taskDTOS, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
