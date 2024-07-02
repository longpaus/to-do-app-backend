package com.todobackend.strategy.GroupTasks;

import com.todobackend.dto.TaskDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

public interface IGroupingTasksStrategy {
    Map<String, List<TaskDTO>> groupTasks(List<TaskDTO> tasks);
}
