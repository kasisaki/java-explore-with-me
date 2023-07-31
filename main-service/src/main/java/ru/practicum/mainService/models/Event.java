package ru.practicum.mainService.models;

import lombok.Data;
import ru.practicum.mainService.utils.enums.StatusEnum;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(length = 2000)
    private String annotation;


    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(length = 10000)
    private String description;

    @Column
    private LocalDateTime createdOn;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;

    @Column
    private Boolean paid;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Column
    private Boolean requestModeration = true; //default value

    @Column
    private String title;

    @Column
    @Enumerated(EnumType.STRING)
    private StatusEnum state;

    @Column
    private LocalDateTime publishedOn;
}
