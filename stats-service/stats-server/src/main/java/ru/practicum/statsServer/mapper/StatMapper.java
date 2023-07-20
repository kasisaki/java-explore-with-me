package ru.practicum.statsServer.mapper;

import ru.practicum.dto.StatHitDto;
import ru.practicum.statsServer.model.StatData;

public class StatMapper {
    public static StatData toStats(StatHitDto hit) {
        return StatData.builder()
                .app(hit.getApp())
                .uri(hit.getUri())
                .created(hit.getTimestamp())
                .ip(hit.getIp())
                .build();
    }
}
