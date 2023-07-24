package ru.practicum.mainService.models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@IdClass(CompilationEventLinker.class)
@Table(name = "compilation_of_events")
public class CompilationEventLinker implements Serializable {
    @Id
    @JoinColumn(table = "events", name = "id")
    private Long eventId;
    @Id
    @JoinColumn(table = "compilations", name = "id")
    private Long compilationId;
}
