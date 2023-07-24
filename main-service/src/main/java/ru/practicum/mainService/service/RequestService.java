package ru.practicum.mainService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mainService.repositories.EventRepository;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final EventRepository eventRepository;

    public void sayHello() {
        System.out.println("Hello");
    }
}
