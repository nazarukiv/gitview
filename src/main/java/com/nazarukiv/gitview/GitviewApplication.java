package com.nazarukiv.gitview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class    GitviewApplication {

    public static void main(String[] args) {
        SpringApplication.run(GitviewApplication.class, args);
    }

}
