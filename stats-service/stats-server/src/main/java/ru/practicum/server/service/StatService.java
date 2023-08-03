package ru.practicum.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.dto.StatHitDto;
import ru.practicum.dto.StatResponseDto;
import ru.practicum.dto.StatResponseShortDto;
import ru.practicum.server.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.server.mapper.StatMapper.statsToDto;
import static ru.practicum.server.mapper.StatMapper.toStats;

@Service
@RequiredArgsConstructor
public class StatService {

    private final StatRepository repository;

    public List<StatResponseDto> getStatistics(LocalDateTime start, LocalDateTime end,
                                               Boolean unique, List<String> uris) {
        if (end.isBefore(start)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End time cannot be before start time");
        }

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

    public StatHitDto hit(StatHitDto hit) {
        return statsToDto(repository.save(toStats(hit)));
    }

    public Map<Long, Long> getHitsOfUris(List<String> uris) {
        List<StatResponseShortDto> listOfDto = repository.getMapOfViewsOfUris(uris);
        return listOfDto.stream()
                .collect(Collectors.toMap(
                        dto -> Long.parseLong(dto.getUri().split("/")[2]), StatResponseShortDto::getHits)
                );
    }
}
