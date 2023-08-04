package ru.practicum.mainService.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainService.utils.enums.StatusEnum;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String text;

    @Column
    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "commenter_id")
    private User commenter;

    @Column
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", created on=" + created +
                ", eventId=" + event.getId() +
                ", requesterId=" + commenter.getId() +
                ", status=" + status +
                '}';
    }
}
