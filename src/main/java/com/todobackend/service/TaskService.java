package com.todobackend.service;

import com.todobackend.model.Task;
import com.todobackend.repository.ITaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService implements ITaskService{
    @Autowired
    ITaskRepository taskRepository;

    public TaskService(ITaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task findTaskById(long id) {
        Optional<Task> task = taskRepository.findById(id);
        return task.orElse(null);
    }

    @Override
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }
    @Override
    public Task updateTask(Task task) {
        // Check if the user exists
        Optional<Task> optionalTask= taskRepository.findById(task.getId());
        if (optionalTask.isPresent()) {
            Task existingTask = optionalTask.get();

            existingTask.setName(task.getName());
            existingTask.setDueDate(task.getDueDate());
            existingTask.setCompleted(task.isCompleted());

            return taskRepository.save(existingTask);
        } else {
            return null;
        }
    }

    @Override
    public Optional<List<Task>> getTasksByUserName(String username) {
        return taskRepository.getTasksByUserName(username);
    }
}
