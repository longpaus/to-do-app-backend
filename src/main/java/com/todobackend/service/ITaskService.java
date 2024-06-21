package com.todobackend.service;

import com.todobackend.dto.CreateTaskDTO;
import com.todobackend.dto.TaskDTO;
import java.util.List;
import java.util.Optional;

public interface ITaskService {
    TaskDTO findTaskById(long id);
    TaskDTO createTask(CreateTaskDTO createTaskDTO);
    TaskDTO updateTask(long taskId, TaskDTO taskDTO);
    List<TaskDTO> getTasksByUserId(long userId);
}
