package com.todobackend.dto;

import lombok.*;

import java.util.Date;
import java.util.Objects;

public class TaskDTO {
    private long id;
    private String name;
    private boolean completed;
    private String dueDate;
    private long userId;

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Check if the objects are the same
        if (o == null || getClass() != o.getClass()) return false; // Check if the object is of the same class
        TaskDTO taskDTO = (TaskDTO) o; // Cast the object to the same class
        return id == taskDTO.id &&
                completed == taskDTO.completed &&
                userId == taskDTO.userId &&
                Objects.equals(name, taskDTO.name) &&
                Objects.equals(dueDate, taskDTO.dueDate);
    }

}
