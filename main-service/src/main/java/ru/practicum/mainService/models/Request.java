package ru.practicum.mainService.models;

import lombok.Builder;
import lombok.Data;
import ru.practicum.mainService.utils.enums.RequestStatusEnum;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@Entity(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime created;

    @ManyToOne(targetEntity = Event.class)
    @JoinColumn(name = "event_id")
    private Long eventId;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "requester_id")
    private Long userId;

    @Column
    @Enumerated(EnumType.STRING)
    private RequestStatusEnum status;
}
