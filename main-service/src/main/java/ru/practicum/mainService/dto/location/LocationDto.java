package ru.practicum.mainService.dto.location;

import lombok.Data;

import java.io.Serializable;

@Data
public class LocationDto implements Serializable {
    private float lat;
    private float lon;
}
