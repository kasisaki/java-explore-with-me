package ru.practicum.server.mapper;

import ru.practicum.dto.StatHitDto;
import ru.practicum.server.model.StatData;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.practicum.utils.Constants.DATE_PATTERN;

public class StatMapper {
    public static StatData toStats(StatHitDto hit) {
        return StatData.builder()
                .app(hit.getApp())
                .uri(hit.getUri())
                .created(LocalDateTime.parse(hit.getTimestamp(), DateTimeFormatter.ofPattern(DATE_PATTERN)))
                .ip(hit.getIp())
                .build();
    }

    public static StatHitDto statsToDto(StatData data) {
        if (data == null) {
            return null;
        }
        return StatHitDto.builder()
                .app(data.getApp())
                .uri(data.getUri())
                .ip(data.getIp())
                .timestamp(data.getCreated().format(DateTimeFormatter.ofPattern(DATE_PATTERN)))
                .build();
    }
}
