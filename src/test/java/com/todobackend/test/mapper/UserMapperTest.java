package com.todobackend.test.mapper;

import com.todobackend.dto.UserDTO;
import com.todobackend.dto.UserFormDTO;
import com.todobackend.mapper.IUserMapper;
import com.todobackend.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    private IUserMapper userMapper;

    @Test
    @Description("test from user to userdto")
    public void testToUserDTO() {
        Date creationDate = new Date(124, Calendar.JUNE, 17);
        User user = new User();
        user.setId(1);
        user.setUsername("userName");
        user.setPassword("password");
        user.setJoinedOn(creationDate);

        UserDTO userDTO = userMapper.userToUserDTO(user);

        assertNotNull(userDTO);
        assertEquals(userDTO.getId(), user.getId());
        assertEquals(userDTO.getUsername(), user.getUsername());
        assertEquals(userDTO.getPassword(), user.getPassword());
        assertEquals("17.06.2024", userDTO.getJoinedOn());
    }

    @Test
    @Description("test from userdto to user")
    public void testToUser() {
        Date creationDate = new Date(124, Calendar.JUNE, 17);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setUsername("userName");
        userDTO.setPassword("password");
        userDTO.setJoinedOn("17.06.2024");

        User user = userMapper.userDTOToUser(userDTO);
        assertNotNull(user);
        assertEquals(userDTO.getId(), user.getId());
        assertEquals(userDTO.getUsername(), user.getUsername());
        assertEquals(userDTO.getPassword(), user.getPassword());
        assertEquals(user.getJoinedOn(), creationDate);
    }

    @Test
    public void testUserFormDTO_toUser() {
        UserFormDTO userFormDTO = new UserFormDTO();
        userFormDTO.setPassword("password");
        userFormDTO.setUsername("username");

        User user = userMapper.userFormDTOToUser(userFormDTO);

        // Assert
        assertEquals(userFormDTO.getUsername(), user.getUsername());
        assertEquals(userFormDTO.getPassword(), user.getPassword());
        assertNotNull(user.getJoinedOn());
    }
}
