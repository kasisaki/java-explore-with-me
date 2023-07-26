package ru.practicum.mainService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.mainService.dto.event.EventFullDto;
import ru.practicum.mainService.dto.event.EventShortDto;
import ru.practicum.mainService.mappers.EventMapper;
import ru.practicum.mainService.repositories.EventRepository;
import ru.practicum.mainService.repositories.UserRepository;
import ru.practicum.mainService.utils.enums.StatusEnum;
import ru.practicum.statsClient.StatClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final StatClient statClient;

    public List<EventShortDto> getEvents(Long userId, Integer from, Integer size) {
        return null;
    }

    public List<EventShortDto> getShortEventsFilter(String text, List<Integer> categoriesId, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable, String sort, int from, int size, HttpServletRequest request) {
        return null;
    }

    public EventFullDto getEvent(int i, HttpServletRequest request) {
        return null;
    }

    public List<EventShortDto> getShortEvents(Long userId, Integer from, Integer size) {
        return null;
    }

    public List<EventFullDto> getFullEvents(Set<Long> userIds, List<StatusEnum> states, List<Long> categories,
                                            LocalDateTime fromDate, LocalDateTime toDate, Integer pagingFrom,
                                            Integer pagingSize) {
        List<EventFullDto> events = eventRepository.getFullEventsFiltered(
                        PageRequest.of(pagingFrom, pagingSize), userIds, states, categories, fromDate, toDate)
                .stream()
                .map(EventMapper::toEventDto)
                .collect(Collectors.toList());


        return null;
    }
}
