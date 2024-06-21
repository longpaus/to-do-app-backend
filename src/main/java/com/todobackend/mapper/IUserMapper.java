package com.todobackend.mapper;

import com.todobackend.dto.UserDTO;
import com.todobackend.dto.UserFormDTO;
import com.todobackend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;


@Mapper(componentModel = "spring")
public interface IUserMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "username", target = "username"),
            @Mapping(source = "password", target = "password"),
            @Mapping(source = "joinedOn", target = "joinedOn", dateFormat = "dd.MM.yyyy")
    })
    UserDTO userToUserDTO(User user);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "username", target = "username"),
            @Mapping(source = "password", target = "password"),
            @Mapping(source = "joinedOn", target = "joinedOn", dateFormat = "dd.MM.yyyy")
    })
    User userDTOToUser(UserDTO userDTO);

    @Mappings({
            @Mapping(source = "username", target = "username"),
            @Mapping(source = "password", target = "password"),
            @Mapping(target = "joinedOn", expression = "java(new java.util.Date())")
    })
    User userFormDTOToUser(UserFormDTO userFormDTO);
}
