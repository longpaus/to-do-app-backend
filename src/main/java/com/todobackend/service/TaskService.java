package com.todobackend.service;

import com.todobackend.dto.CreateTaskDTO;
import com.todobackend.dto.TaskDTO;
import com.todobackend.exception.IdNotFoundException;
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
    public TaskDTO findTaskById(long taskId) {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new IdNotFoundException("Task Id not found"));

        return taskMapper.taskToTaskDTO(existingTask);
    }

    @Override
    public TaskDTO createTask(CreateTaskDTO createTaskDTO) {
        User user = userRepository.findById(createTaskDTO.getUserId())
                .orElseThrow(() -> new IdNotFoundException("User Id not found"));

        Task task = taskMapper.createTaskDTOtoTask(createTaskDTO, user);
        return taskMapper.taskToTaskDTO(taskRepository.save(task));
    }

    @Override
    public TaskDTO updateTask(long taskId, TaskDTO taskDTO) {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new IdNotFoundException("Task id not found"));

        // Ensure the user exists
        User user = userRepository.findById(taskDTO.getUserId())
                .orElseThrow(() -> new IdNotFoundException("User not found"));

        Task updatedTask = taskMapper.taskDTOToTask(taskDTO,user);
        updatedTask.setId(taskId);
        updatedTask.setUser(user);

        Task savedTask = taskRepository.save(updatedTask);
        return taskMapper.taskToTaskDTO(savedTask);
    }

    @Override
    public List<TaskDTO> getTasksByUserId(long userId) {
        List<Task> tasks = taskRepository.getTasksByUserId(userId);

        List<TaskDTO> taskDTOS = new ArrayList<>();
        for(Task task : tasks){
            taskDTOS.add(taskMapper.taskToTaskDTO(task));
        }
        return taskDTOS;
    }

}
