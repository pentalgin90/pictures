package ru.home.aws.pictures;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PicturesStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(PicturesStoreApplication.class, args);
    }
}
