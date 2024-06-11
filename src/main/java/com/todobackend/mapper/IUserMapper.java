package com.todobackend.mapper;

import com.todobackend.dto.UserDTO;
import com.todobackend.model.User;
import org.mapstruct.Mapper;

@Mapper
public interface IUserMapper {
    UserDTO toDTO(User user);

    User fromDTO(UserDTO userDTO);
}
