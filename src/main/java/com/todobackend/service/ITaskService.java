package com.todobackend.service;

import com.todobackend.model.Task;

import java.util.List;
import java.util.Optional;

public interface ITaskService {
    Task findTaskById(long id);
    Task createTask(Task task);
    Task updateTask(Task task);
    Optional<List<Task>> getTasksByUserName(String username);
}
