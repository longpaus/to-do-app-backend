package com.todobackend.mapper;

import com.todobackend.dto.UserDTO;
import com.todobackend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ValueMapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface IUserMapper {

    @Mappings({
            @Mapping(source = "id", target="id"),
            @Mapping(source = "username", target="username"),
            @Mapping(source= "password", target = "password"),
            @Mapping(source = "joinedOn", target = "joinedOn", dateFormat = "dd.MM.yyyy")
    })
    UserDTO toDTO(User user);

    @Mappings({
            @Mapping(source = "id", target="id"),
            @Mapping(source = "username", target="username"),
            @Mapping(source= "password", target = "password"),
            @Mapping(source = "joinedOn", target = "joinedOn", dateFormat = "dd.MM.yyyy")
    })
    User fromDTO(UserDTO userDTO);
}
