package com.todobackend.test.strategy;

import com.todobackend.dto.TaskDTO;
import com.todobackend.strategy.GroupTasks.GroupTasksByDate;
import com.todobackend.strategy.GroupTasks.IGroupingTasksStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class GroupTasksByDateTest {

    private IGroupingTasksStrategy groupTasksByDate;

    @BeforeEach
    public void setUp() {
        groupTasksByDate = new GroupTasksByDate();
    }

    @Test
    public void testGroupTasksByDate() {
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

        TaskDTO task3 = new TaskDTO();
        task3.setId(3L);
        task3.setName("Task 3, completed");
        task3.setDueDate(tomorrowString);
        task3.setCompleted(true);

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

        List<TaskDTO> tasks = List.of(task1, task2, task3, task4,task5, task6, task7);

        // Group tasks
        Map<String, List<TaskDTO>> groupedTasks = groupTasksByDate.groupTasks(tasks);

        // Assertions
        assertEquals(1, groupedTasks.get("Today").size());
        assertEquals(1, groupedTasks.get("Later").size());
        assertEquals(1, groupedTasks.get("No Date").size());
        assertEquals(1,groupedTasks.get("Next 7 Days").size());
        assertEquals(1, groupedTasks.get("Completed").size());
        assertEquals(1, groupedTasks.get("Overdue").size());
        assertEquals(1, groupedTasks.get("Tomorrow").size());

        assertTrue(groupedTasks.get("Today").contains(task1));
        assertTrue(groupedTasks.get("Later").contains(task6));
        assertTrue(groupedTasks.get("Overdue").contains(task5));
        assertTrue(groupedTasks.get("Next 7 Days").contains(task2));
        assertTrue(groupedTasks.get("No Date").contains(task4));
        assertTrue(groupedTasks.get("Completed").contains(task3));
        assertTrue(groupedTasks.get("Tomorrow").contains(task7));
    }
}

