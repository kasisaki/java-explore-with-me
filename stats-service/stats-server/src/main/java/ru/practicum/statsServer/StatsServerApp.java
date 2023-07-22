package ru.practicum.statsServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories("ru.practicum.statsServer")
public class StatsServerApp {

    public static void main(String[] args) {
        SpringApplication.run(StatsServerApp.class, args);
    }

}