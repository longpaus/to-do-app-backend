package com.todobackend.strategy;

import com.todobackend.dto.TaskDTO;
import com.todobackend.model.Task;
import org.springframework.stereotype.Component;

import java.util.List;

public interface ISortingTasksStrategy {
    void sortTasks(List<TaskDTO> tasks);
}
