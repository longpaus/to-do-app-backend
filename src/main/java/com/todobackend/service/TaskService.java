package com.todobackend.service;

import com.todobackend.dto.TaskDTO;
import com.todobackend.mapper.ITaskMapper;
import com.todobackend.model.Task;
import com.todobackend.model.User;
import com.todobackend.repository.ITaskRepository;
import com.todobackend.repository.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService implements ITaskService{
    ITaskRepository taskRepository;

    IUserRepository userRepository;

    ITaskMapper taskMapper;

    @Override
    public Optional<TaskDTO> findTaskById(long taskId) {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        return Optional.of(taskMapper.taskToTaskDTO(existingTask));
    }

    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        User user = userRepository.findById(taskDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = taskMapper.taskDTOToTask(taskDTO, user);
        task.setUser(user);

        return taskMapper.taskToTaskDTO(taskRepository.save(task));
    }

    @Override
    public Optional<TaskDTO> updateTask(long taskId, TaskDTO taskDTO) {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Ensure the user exists
        User user = userRepository.findById(taskDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task updatedTask = taskMapper.taskDTOToTask(taskDTO,user);
        updatedTask.setId(taskId);
        updatedTask.setUser(user);

        Task savedTask = taskRepository.save(updatedTask);
        return Optional.of(taskMapper.taskToTaskDTO(savedTask));
    }



    @Override
    public Optional<List<TaskDTO>> getTasksByUserName(String username) {
        List<Task> existingTasks = taskRepository.getTasksByUserName(username)
                .orElseThrow(() -> new RuntimeException("username not found"));

        List<TaskDTO> taskDTOS = new ArrayList<>();
        for (Task task : existingTasks) {
            taskDTOS.add(taskMapper.taskToTaskDTO(task));
        }
        return Optional.of(taskDTOS);
    }
}
