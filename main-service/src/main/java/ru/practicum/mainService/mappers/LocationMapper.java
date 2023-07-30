package ru.practicum.mainService.mappers;

import ru.practicum.mainService.dto.location.LocationDto;
import ru.practicum.mainService.models.Location;

public class LocationMapper {
    public static final Location dtoToLocation(LocationDto dto) {
        if (dto == null) {
            return null;
        }
        return Location.builder()
                .lat(dto.getLat())
                .lon(dto.getLon())
                .build();
    }
}
