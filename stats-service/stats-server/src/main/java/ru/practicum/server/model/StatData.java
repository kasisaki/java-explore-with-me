package ru.practicum.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@Entity
@Builder
@Table(name = "stats_data")
@AllArgsConstructor
@RequiredArgsConstructor
public class StatData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uri")
    private String uri;

    @Column(name = "ip")
    private String ip;

    @Column(name = "app")
    private String app;

    @Column(name = "created")
    private LocalDateTime created;

    @Transient
    private Long histCount;

}
