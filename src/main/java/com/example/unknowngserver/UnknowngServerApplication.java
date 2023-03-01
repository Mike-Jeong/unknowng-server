package com.example.unknowngserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class UnknowngServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UnknowngServerApplication.class, args);
    }

}
