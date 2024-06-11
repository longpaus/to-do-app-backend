package com.todobackend.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {
    private long id;

    private String username;

    private String password;

    private String joinedOn;
}
