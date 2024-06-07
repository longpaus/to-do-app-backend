package com.todobackend.repository;

import com.todobackend.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ITaskRepository extends JpaRepository<Task, Long> {

    @Query(nativeQuery = true, value = "Select * From tasks Where user_name = ?1")
    Optional<List<Task>> getTasksByUserName(String username);
}
