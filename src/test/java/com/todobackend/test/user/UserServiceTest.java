package com.todobackend.test.user;

import com.todobackend.dto.UserDTO;
import com.todobackend.exception.IdNotFoundException;
import com.todobackend.exception.UserNameExistException;
import com.todobackend.exception.UserNameNotFoundException;
import com.todobackend.mapper.IUserMapper;
import com.todobackend.model.User;
import com.todobackend.repository.IUserRepository;
import com.todobackend.service.IUserService;
import com.todobackend.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IUserMapper userMapper;

    @Test()
    void testCreateUser() {
        // arrange
        Date creationDate = new Date(124, Calendar.JUNE,17);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setUsername("userName");
        userDTO.setPassword("password");
        userDTO.setJoinedOn("17.06.2024");

        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setJoinedOn(creationDate);

        when(userMapper.fromDTO(userDTO)).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);
        when(userRepository.save(user)).thenReturn(user);


        // act
        UserDTO resDTO = userService.createUser(userDTO);

        // assert
        assertEquals(userDTO.getUsername(), resDTO.getUsername());
        assertEquals(userDTO.getPassword(), resDTO.getPassword());
        assertEquals(userDTO.getJoinedOn(), resDTO.getJoinedOn());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testCreateUserThrowException() {
        // arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("existUsername");

        User user = new User();
        user.setUsername(userDTO.getUsername());
        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.of(user));

        // act & assert
        assertThrows(UserNameExistException.class, () -> userService.createUser(userDTO));

        verify(userRepository, times(1)).findByUsername(userDTO.getUsername());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetUserById() {
        // arrange
        long userId = 1L;
        Date creationDate = new Date(124, Calendar.JUNE, 17);
        User user = new User();
        user.setId(userId);
        user.setUsername("userName");
        user.setPassword("password");
        user.setJoinedOn(creationDate);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setUsername("userName");
        userDTO.setPassword("password");
        userDTO.setJoinedOn("17.06.2024");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        // act
        UserDTO result = userService.getUserById(userId);

        // assert
        assertEquals(userDTO.getUsername(), result.getUsername());
        assertEquals(userDTO.getPassword(), result.getPassword());
        assertEquals(userDTO.getJoinedOn(), result.getJoinedOn());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testGetUserByIdThrowException() {
        // arrange
        long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(IdNotFoundException.class, () -> userService.getUserById(userId));

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testGetUserByUsername() {
        // arrange
        String username = "userName";
        Date creationDate = new Date(124, Calendar.JUNE, 17);
        User user = new User();
        user.setId(1L);
        user.setUsername(username);
        user.setPassword("password");
        user.setJoinedOn(creationDate);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername(username);
        userDTO.setPassword("password");
        userDTO.setJoinedOn("17.06.2024");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        // act
        UserDTO result = userService.getUserByUsername(username);

        // assert
        assertEquals(userDTO.getUsername(), result.getUsername());
        assertEquals(userDTO.getPassword(), result.getPassword());
        assertEquals(userDTO.getJoinedOn(), result.getJoinedOn());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testGetUserByUsernameThrowException() {
        // arrange
        String username = "existUsername";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(UserNameNotFoundException.class, () -> userService.getUserByUsername(username));

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testUpdateUser() {
        // arrange
        long userId = 1L;
        Date creationDate = new Date(124, Calendar.JUNE, 17);
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("userName");
        existingUser.setPassword("password");
        existingUser.setJoinedOn(creationDate);

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setUsername("updatedName");
        updatedUser.setPassword("newPassword");
        updatedUser.setJoinedOn(creationDate);

        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setId(userId);
        updatedUserDTO.setUsername("updatedName");
        updatedUserDTO.setPassword("newPassword");
        updatedUserDTO.setJoinedOn("17.06.2024");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(updatedUser);
        when(userMapper.toDTO(updatedUser)).thenReturn(updatedUserDTO);

        // act
        UserDTO result = userService.updateUser(userId, updatedUser);

        // assert
        assertEquals(updatedUserDTO.getUsername(), result.getUsername());
        assertEquals(updatedUserDTO.getPassword(), result.getPassword());
        assertEquals(updatedUserDTO.getJoinedOn(), result.getJoinedOn());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testUpdateUserThrowException() {
        // arrange
        long userId = 1L;
        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setUsername("updatedName");
        updatedUser.setPassword("newPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(IdNotFoundException.class, () -> userService.updateUser(userId, updatedUser));

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testDeleteUser() {
        // arrange
        long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("userName");
        user.setPassword("password");
        user.setJoinedOn(new Date());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // act
        userService.deleteUser(userId);

        // assert
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testDeleteUserThrowException() {
        // arrange
        long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(IdNotFoundException.class, () -> userService.deleteUser(userId));

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).deleteById(userId);
    }
}
