package com.todobackend.mapper;

import com.todobackend.dto.TaskDTO;
import com.todobackend.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ITaskMapper {

    @Mapping(target = "userId", expression = "java(task.getUser().getId())")
    TaskDTO toDTO(Task task);

    @Mapping(target = "id", ignore = true)
    Task fromDTO(TaskDTO taskDTO);

}
