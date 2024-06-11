package com.todobackend.repository;

import com.todobackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {

    @Query(nativeQuery = true, value = "Select * From users Where user_name = ?1")
    Optional<User> findByUsername(String username);
}
