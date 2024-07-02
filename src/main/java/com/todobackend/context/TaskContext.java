package com.todobackend.context;

import com.todobackend.dto.TaskDTO;
import com.todobackend.strategy.GroupTasks.IGroupingTasksStrategy;
import com.todobackend.strategy.ISortingTasksStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class TaskContext {
    private final IGroupingTasksStrategy groupingStrategy;

    private  final ISortingTasksStrategy sortingStrategy;

    public TaskContext(IGroupingTasksStrategy groupingStrategy, ISortingTasksStrategy sortingStrategy) {
        this.groupingStrategy = groupingStrategy;
        this.sortingStrategy = sortingStrategy;
    }

    public Map<String, List<TaskDTO>> executeStrategy(List<TaskDTO> tasks) {
        Map<String, List<TaskDTO>> groupedTasks = groupingStrategy.groupTasks(tasks);
        groupedTasks.values().forEach(sortingStrategy::sortTasks);
        return groupedTasks;
    }
}
