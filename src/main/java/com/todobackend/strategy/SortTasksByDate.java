package com.todobackend.strategy;


import com.todobackend.dto.TaskDTO;
import com.todobackend.model.Task;
import org.springframework.stereotype.Component;

import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
public class SortTasksByDate implements ISortingTasksStrategy{
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    public void sortTasks(List<TaskDTO> tasks) {
        tasks.sort(Comparator.comparing(
                task -> {
                    if (task.getDueDate() == null || task.getDueDate().isEmpty()) {
                        return null;
                    }
                    return LocalDate.parse(task.getDueDate(), DATE_FORMATTER);
                },
                Comparator.nullsLast(Comparator.naturalOrder())
        )
        );

    }
}
