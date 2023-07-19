package ru.practicum.statsServer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@Entity
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class StatData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uri")
    private String uri;

    @Column(name = "IP")
    private String IP;

    @Column(name = "app")
    private String app;

    @Column(name = "created")
    private LocalDateTime created;

    @Transient
    private Long histCount;

}
