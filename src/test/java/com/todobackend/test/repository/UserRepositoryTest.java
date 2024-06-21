package com.todobackend.test.repository;

import com.todobackend.model.User;
import com.todobackend.repository.IUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Description;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    IUserRepository userRepository;

    @Test
    @Description("find username and return the optional user")
    public void testFindByUsername() {
        // arrange
        Date creationDate = new Date(124, Calendar.JUNE, 17);
        User user = new User();
        user.setUsername("username");
        user.setJoinedOn(creationDate);
        user.setPassword("password");
        User savedUser = userRepository.save(user);

        // act
        Optional<User> res = userRepository.findByUsername(savedUser.getUsername());

        // assert
        assertTrue(res.isPresent());
        assertEquals(savedUser.getId(), res.get().getId());
        assertEquals(savedUser.getUsername(), res.get().getUsername());
        assertEquals(savedUser.getPassword(), res.get().getPassword());
        assertEquals(creationDate, res.get().getJoinedOn());
    }

    @Test
    @Description("username doesn't exist")
    void testFindByUsername_notFound() {
        // arrange
        Date creationDate = new Date(124, Calendar.JUNE, 17);
        User user = new User();
        user.setUsername("username");
        user.setJoinedOn(creationDate);
        user.setPassword("password");
        userRepository.save(user);

        // act
        Optional<User> res = userRepository.findByUsername("doesnotexist");

        // assert
        assertTrue(res.isEmpty());
    }
}
