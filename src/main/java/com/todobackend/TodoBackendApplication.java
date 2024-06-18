package com.todobackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class TodoBackendApplication {

    public static void main(String[] args) {
        System.out.println("hello world");
        SpringApplication.run(TodoBackendApplication.class, args);
    }

}
