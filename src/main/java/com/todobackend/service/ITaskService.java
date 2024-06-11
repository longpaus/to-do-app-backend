package com.todobackend.service;

import com.todobackend.dto.TaskDTO;
import java.util.List;
import java.util.Optional;

public interface ITaskService {
    Optional<TaskDTO> findTaskById(long id);
    TaskDTO createTask(TaskDTO taskDTO);
    Optional<TaskDTO> updateTask(long taskId, TaskDTO taskDTO);
    Optional<List<TaskDTO>> getTasksByUserName(String username);
}
