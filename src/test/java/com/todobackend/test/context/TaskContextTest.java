package com.todobackend.test.context;

import com.todobackend.context.TaskContext;
import com.todobackend.dto.TaskDTO;
import com.todobackend.strategy.GroupTasks.IGroupingTasksStrategy;
import com.todobackend.strategy.ISortingTasksStrategy;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TaskContextTest {

    @Mock
    private IGroupingTasksStrategy groupingStrategy;

    @Mock
    private ISortingTasksStrategy sortingStrategy;

    @Test
    public void testExecuteStrategy() {
        TaskContext taskContext = new TaskContext(groupingStrategy, sortingStrategy);
        // arrange
        LocalDate today = LocalDate.now();
        LocalDate in5Days = today.plusDays(5);
        LocalDate in6Days = today.plusDays(6);


        // Define the date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        // Format the LocalDate to a string
        String todayString = today.format(formatter);
        String in5DaysString = in5Days.format(formatter);
        String in6DaysString = in6Days.format(formatter);

        // Create sample tasks
        TaskDTO task1 = new TaskDTO();
        task1.setId(1L);
        task1.setName("Task 1");
        task1.setDueDate(todayString);
        task1.setCompleted(false);

        TaskDTO task2 = new TaskDTO();
        task2.setId(2L);
        task2.setName("Task 2");
        task2.setDueDate(in5DaysString);
        task2.setCompleted(false);

        TaskDTO task3 = new TaskDTO();
        task3.setId(3L);
        task3.setName("Task 3");
        task3.setDueDate(in6DaysString);
        task3.setCompleted(false);

        List<TaskDTO> tasks = Arrays.asList(task3, task2, task1);

        Map<String, List<TaskDTO>> groupedTasks = new HashMap<>();
        groupedTasks.put("Today", Arrays.asList(task1));
        groupedTasks.put("Next 7 Days", Arrays.asList(task2, task3));

        when(groupingStrategy.groupTasks(tasks)).thenReturn(groupedTasks);

        // act
        Map<String, List<TaskDTO>> result = taskContext.executeStrategy(tasks);

        verify(groupingStrategy, times(1)).groupTasks(tasks);
        verify(sortingStrategy, times(1)).sortTasks(groupedTasks.get("Today"));
        verify(sortingStrategy, times(1)).sortTasks(groupedTasks.get("Next 7 Days"));

        assertNotNull(result);
        assertEquals(groupedTasks, result);

    }
}
