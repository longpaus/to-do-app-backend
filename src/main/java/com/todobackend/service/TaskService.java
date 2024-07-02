package com.todobackend.service;

import com.todobackend.context.TaskContext;
import com.todobackend.dto.CreateTaskDTO;
import com.todobackend.dto.TaskDTO;
import com.todobackend.exception.IdNotFoundException;
import com.todobackend.exception.InvalidStrategyException;
import com.todobackend.mapper.ITaskMapper;
import com.todobackend.model.Task;
import com.todobackend.model.User;
import com.todobackend.repository.ITaskRepository;
import com.todobackend.repository.IUserRepository;
import com.todobackend.strategy.GroupTasks.GroupTasksByDate;
import com.todobackend.strategy.GroupTasks.IGroupingTasksStrategy;
import com.todobackend.strategy.ISortingTasksStrategy;
import com.todobackend.strategy.SortTasksByDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TaskService implements ITaskService{
    @Autowired
    ITaskRepository taskRepository;

    @Autowired
    IUserRepository userRepository;

    @Autowired
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

    @Override
    public Map<String, List<TaskDTO>> getTaskGroupsByUserId(long userId, String groupBy, String sortBy) {
        List<Task> tasks = taskRepository.getTasksByUserId(userId);
        List<TaskDTO> taskDTOs = tasks.stream().map(taskMapper::taskToTaskDTO).toList();
        IGroupingTasksStrategy groupingStrategy = getGroupingStrategy(groupBy);
        ISortingTasksStrategy sortingTasksStrategy = getSortingTasksStrategy(sortBy);

        if(groupingStrategy == null || sortingTasksStrategy == null){
            throw new InvalidStrategyException("invalid soring or grouping strategy");
        }
        TaskContext context = new TaskContext(groupingStrategy, sortingTasksStrategy);
        return context.executeStrategy(taskDTOs);
    }
    private ISortingTasksStrategy getSortingTasksStrategy(String sortBy) {
        switch (sortBy) {
            case "date":
                return new SortTasksByDate();
            default:
                return null;
        }
    }
    private IGroupingTasksStrategy getGroupingStrategy(String groupBy) {
        switch (groupBy) {
            case "date":
                return new GroupTasksByDate();
            default:
                return null;
        }

    }

}
