package com.todobackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class TaskDTO {
    private long id;
    private String name;
    private boolean completed;
    private String dueDate;
    private long userId;
}
