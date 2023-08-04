package ru.practicum.mainService.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.mainService.utils.enums.EventStatusEnum;

import javax.persistence.*;
import java.time.LocalDateTime;

import static ru.practicum.utils.Constants.DATE_PATTERN;

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


    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(length = 10000)
    private String description;

    @Column
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDateTime createdOn;

    @Column(name = "event_date")
    @JsonFormat(pattern = DATE_PATTERN)
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
    private EventStatusEnum state;

    @Column
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDateTime publishedOn;

    @Column
    private Long views;

    @Column
    private Integer confirmedRequests;

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", categoryId=" + category.getId() +
                ", eventDate=" + eventDate +
                ", initiatorId=" + initiator.getId() +
                ", title='" + title + '\'' +
                ", state=" + state +
                ", views=" + views +
                ", confirmedRequests=" + confirmedRequests +
                '}';
    }
}
