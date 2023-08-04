package ru.practicum.mainService.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "compilations")
public class Compilation {
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "compilation_of_events", joinColumns = {@JoinColumn(name = "compilation_id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id")})
    Set<Event> events;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private boolean pinned;
    @Column
    private String title;

    @Override
    public String toString() {
        return "Compilation{" +
                "eventIds=" + events.stream().map(Event::getId).collect(Collectors.toList()) +
                ", id=" + id +
                ", pinned=" + pinned +
                ", title='" + title + '\'' +
                '}';
    }
}
