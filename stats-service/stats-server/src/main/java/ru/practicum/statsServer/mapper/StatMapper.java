package ru.practicum.statsServer.mapper;

import ru.practicum.dto.StatHitDto;
import ru.practicum.statsServer.model.StatData;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StatMapper {
    public static StatData toStats(StatHitDto hit) {
        return StatData.builder()
                .app(hit.getApp())
                .uri(hit.getUri())
                .created(LocalDateTime.parse(hit.getTimestamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .ip(hit.getIp())
                .build();
    }
}
