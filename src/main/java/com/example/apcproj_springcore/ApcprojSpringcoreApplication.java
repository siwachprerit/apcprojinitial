package com.example.apcproj_springcore;

import com.example.apcproj_springcore.console.ConsoleRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApcprojSpringcoreApplication implements CommandLineRunner {

    private final ConsoleRunner consoleRunner;

    public ApcprojSpringcoreApplication(ConsoleRunner consoleRunner) {
        this.consoleRunner = consoleRunner; // injected by Spring
    }

    public static void main(String[] args) {
        SpringApplication.run(ApcprojSpringcoreApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        consoleRunner.run();
    }
}
