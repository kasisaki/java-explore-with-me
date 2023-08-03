package ru.practicum.statsServer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.StatHitDto;
import ru.practicum.dto.StatResponseDto;
import ru.practicum.dto.StatResponseShortDto;
import ru.practicum.statsServer.mapper.StatMapper;
import ru.practicum.statsServer.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatService {

    private final StatRepository repository;

    public List<StatResponseDto> getStatistics(LocalDateTime start, LocalDateTime end,
                                               Boolean unique, List<String> uris) {
        List<StatResponseDto> response;
        if (!unique) {
            if (uris != null && !uris.isEmpty()) {
                response = repository.getStatsByUris(start, end, uris);
            } else {
                response = repository.getStats(start, end);
            }
        } else {
            if (uris != null && !uris.isEmpty()) {
                response = repository.getStatsWithUniqueIpByUris(start, end, uris);
            } else {
                response = repository.getStatsWithUniqueIp(start, end);
            }
        }

        return response;
    }

    public void hit(StatHitDto hit) {
        repository.save(StatMapper.toStats(hit));
    }

    public Map<Long, Long> getHitsOfEvent(List<String> uris) {
        List<StatResponseShortDto> listOfDto = repository.getMapOfViewsOfEvents(uris);
        return listOfDto.stream()
                .collect(Collectors.toMap(
                        dto -> Long.parseLong(dto.getUri().split("/")[2]), StatResponseShortDto::getHits)
                );
    }
}
