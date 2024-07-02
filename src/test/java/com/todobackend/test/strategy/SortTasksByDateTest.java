package com.todobackend.test.strategy;

import com.todobackend.dto.TaskDTO;
import com.todobackend.strategy.ISortingTasksStrategy;
import com.todobackend.strategy.SortTasksByDate;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SortTasksByDateTest {
    private ISortingTasksStrategy sortingTasksByDate;

    @BeforeEach
    public void setUp() {
        sortingTasksByDate = new SortTasksByDate();
    }

    @Test
    public void testSortTasksByDate() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate next7Days = today.plusDays(5);
        LocalDate overdue = today.plusDays(-5);
        LocalDate later = today.plusDays(8);

        // Define the date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        // Format the LocalDate to a string
        String todayString = today.format(formatter);
        String next7DaysString = next7Days.format(formatter);
        String overdueString = overdue.format(formatter);
        String laterString = later.format(formatter);
        String tomorrowString = tomorrow.format(formatter);

        // Create sample tasks
        TaskDTO task1 = new TaskDTO();
        task1.setId(1L);
        task1.setName("Task 1");
        task1.setDueDate(todayString);
        task1.setCompleted(false);

        TaskDTO task2 = new TaskDTO();
        task2.setId(2L);
        task2.setName("Task 2");
        task2.setDueDate(next7DaysString);
        task2.setCompleted(false);


        TaskDTO task4 = new TaskDTO();
        task4.setId(4L);
        task4.setName("Task 4");
        task4.setDueDate(null);
        task4.setCompleted(false);

        TaskDTO task5 = new TaskDTO();
        task5.setId(5L);
        task5.setName("Task 5");
        task5.setDueDate(overdueString);
        task5.setCompleted(false);

        TaskDTO task6 = new TaskDTO();
        task6.setId(6L);
        task6.setName("Task 6");
        task6.setDueDate(laterString);
        task6.setCompleted(false);

        TaskDTO task7 = new TaskDTO();
        task7.setId(3L);
        task7.setName("Task 7");
        task7.setDueDate(tomorrowString);
        task7.setCompleted(false);

        List<TaskDTO> tasks = new ArrayList<>(Arrays.asList(task1, task2, task4, task5, task6, task7));

        List<TaskDTO> expected = new ArrayList<>(Arrays.asList(task5,task1,task7,task2,task6, task4));

        sortingTasksByDate.sortTasks(tasks);

       assertTrue(tasks.equals(expected));

    }
}
