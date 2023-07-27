package ru.practicum.mainService.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lat")
    private float latitude;

    @Column(name = "lon")
    private float longitude;
}
