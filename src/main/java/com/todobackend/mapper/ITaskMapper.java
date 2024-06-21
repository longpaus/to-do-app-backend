package com.todobackend.mapper;

import com.todobackend.dto.CreateTaskDTO;
import com.todobackend.dto.TaskDTO;
import com.todobackend.model.Task;
import com.todobackend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ITaskMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "completed", target = "completed"),
            @Mapping(source = "dueDate", target = "dueDate", dateFormat = "dd.MM.yyyy"),
            @Mapping(source = "name", target = "name"),
            @Mapping(target = "userId", expression = "java(task.getUser().getId())")
    })
    TaskDTO taskToTaskDTO(Task task);

    @Mappings({
            @Mapping(source = "taskDTO.id", target = "id"),
            @Mapping(source = "taskDTO.completed", target = "completed"),
            @Mapping(source = "taskDTO.dueDate", target = "dueDate", dateFormat = "dd.MM.yyyy"),
            @Mapping(source = "taskDTO.name", target = "name"),
            @Mapping(target = "user", source = "user")
    })
    Task taskDTOToTask(TaskDTO taskDTO, User user);

    @Mappings({
            @Mapping(target = "createTaskDTO.completed",defaultValue = "false"),
            @Mapping(source = "createTaskDTO.dueDate", target = "dueDate", dateFormat = "dd.MM.yyyy"),
            @Mapping(source = "createTaskDTO.name", target = "name"),
            @Mapping(target = "user", source = "user")
    })
    Task createTaskDTOtoTask(CreateTaskDTO createTaskDTO, User user);
}
