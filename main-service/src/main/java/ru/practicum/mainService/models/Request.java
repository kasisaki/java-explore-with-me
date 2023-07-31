package ru.practicum.mainService.models;

import lombok.Data;
import ru.practicum.mainService.utils.enums.StatusEnum;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private User requester;

    @Column
    @Enumerated(EnumType.STRING)
    private StatusEnum status;
}
