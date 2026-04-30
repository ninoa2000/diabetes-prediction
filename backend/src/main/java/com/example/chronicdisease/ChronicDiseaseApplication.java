package com.example.chronicdisease;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class ChronicDiseaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChronicDiseaseApplication.class, args);
    }
} 