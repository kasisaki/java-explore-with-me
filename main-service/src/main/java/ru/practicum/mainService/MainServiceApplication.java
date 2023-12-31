package ru.practicum.mainService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"ru.practicum.mainService", "ru.practicum.statsClient"})
@EnableJpaRepositories("ru.practicum.mainService.*")
@EntityScan("ru.practicum.mainService.*")
public class MainServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainServiceApplication.class, args);
    }

}
