package ru.practicum.mainService.mappers;

import ru.practicum.mainService.dto.location.LocationDto;
import ru.practicum.mainService.models.Location;

public class LocationMapper {

    private LocationMapper() {
        throw new java.lang.UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Location dtoToLocation(LocationDto dto) {
        if (dto == null) {
            return null;
        }
        return Location.builder()
                .lat(dto.getLat())
                .lon(dto.getLon())
                .build();
    }
}
