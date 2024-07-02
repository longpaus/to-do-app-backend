package com.todobackend.strategy.GroupTasks;

import com.todobackend.dto.TaskDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class GroupTasksByDate implements IGroupingTasksStrategy {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    @Override
    public Map<String, List<TaskDTO>> groupTasks(List<TaskDTO> tasks) {

        Map<String, List<TaskDTO>> groupedTasks = new HashMap<>();

        groupedTasks.put("Today", new ArrayList<TaskDTO>());
        groupedTasks.put("Tomorrow", new ArrayList<>());
        groupedTasks.put("Later", new ArrayList<TaskDTO>());
        groupedTasks.put("Overdue", new ArrayList<TaskDTO>());
        groupedTasks.put("No Date", new ArrayList<TaskDTO>());
        groupedTasks.put("Completed", new ArrayList<TaskDTO>());
        groupedTasks.put("Next 7 Days", new ArrayList<>());

        LocalDate today = LocalDate.now();
        LocalDate next7Days = today.plusDays(7);
        LocalDate tomorrow = today.plusDays(1);
        for (TaskDTO task : tasks) {
            if(task.getDueDate() == null || task.getDueDate().isEmpty()) {
                groupedTasks.get("No Date").add(task);
                continue;
            }
            LocalDate dueDate = LocalDate.parse(task.getDueDate(), DATE_FORMATTER);
            if(task.isCompleted()) {
                groupedTasks.get("Completed").add(task);
            } else if(today.isEqual(dueDate)) {
                groupedTasks.get("Today").add(task);
            } else if(today.isBefore(dueDate)) {
                if(dueDate.isEqual(tomorrow)) {
                    groupedTasks.get("Tomorrow").add(task);
                } else if(dueDate.isBefore(next7Days)) {
                    groupedTasks.get("Next 7 Days").add(task);
                } else {
                    groupedTasks.get("Later").add(task);
                }
            } else if(today.isAfter(dueDate)) {
                groupedTasks.get("Overdue").add(task);
            }
        }
        return groupedTasks;
    }
}
