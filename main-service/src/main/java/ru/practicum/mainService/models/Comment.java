package ru.practicum.mainService.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

import static ru.practicum.utils.Constants.DATE_PATTERN;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String text;

    @Column
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDateTime created;

    @Column
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDateTime updated;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "commenter_id")
    private User commenter;

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", created on=" + created +
                ", eventId=" + event.getId() +
                ", requesterId=" + commenter.getId() +
                '}';
    }
}
