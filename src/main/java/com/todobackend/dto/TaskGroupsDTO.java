package com.todobackend.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TaskGroupsDTO {
    private Map<String, List<TaskDTO>> groups;

    public Map<String, List<TaskDTO>> getGroups() {
        return groups;
    }

    public void setGroups(Map<String, List<TaskDTO>> groups) {
        this.groups = groups;
    }
}
