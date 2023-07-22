package ru.practicum.statsServer.mapper;

import ru.practicum.dto.StatHitDto;
import ru.practicum.statsServer.model.StatData;

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
}
