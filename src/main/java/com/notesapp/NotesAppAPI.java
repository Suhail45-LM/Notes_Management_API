package com.notesapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NotesAppAPI {

    public static void main(String[] args) {
        SpringApplication.run(NotesAppAPI.class, args);
    }
}
