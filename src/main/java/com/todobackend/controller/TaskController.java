package com.todobackend.controller;

import com.todobackend.model.Task;
import com.todobackend.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/task/create")
    ResponseEntity<Task> createTask(@RequestBody Task task) {
        return new ResponseEntity<>(taskService.createTask(task),HttpStatus.CREATED);
    }

    @PostMapping("/task/update")
    ResponseEntity<Task> updateTask(@RequestBody Task task) {
        return new ResponseEntity<>(taskService.updateTask(task),HttpStatus.OK);
    }

    @PostMapping("/tasks")
    ResponseEntity<List<Task>> getAllTasks(@RequestBody String userName) {
        Optional<List<Task>> optionalTasks = taskService.getTasksByUserName(userName);
        if(optionalTasks.isPresent()) {
            return new ResponseEntity<>(optionalTasks.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
