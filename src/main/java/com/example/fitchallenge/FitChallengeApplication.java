package com.example.fitchallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class FitChallengeApplication {

    public static void main(String[] args) {
        // Add shutdown hook to properly close HikariCP connection pool
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down application...");
        }));
        
        SpringApplication app = new SpringApplication(FitChallengeApplication.class);
        app.setRegisterShutdownHook(true);
        app.run(args);
        System.out.println("Hello word");
    }

}
