package ru.practicum.mainService.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.mainService.dto.event.EventFullDto;
import ru.practicum.mainService.dto.event.EventShortDto;
import ru.practicum.mainService.mappers.EventMapper;
import ru.practicum.mainService.repositories.EventRepository;
import ru.practicum.mainService.repositories.UserRepository;
import ru.practicum.mainService.utils.enums.StatusEnum;
import ru.practicum.mainService.utils.exceptions.ElementNotFoundException;
import ru.practicum.statsClient.StatClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final StatClient statClient;

    public List<EventShortDto> getEvents(Long userId, Integer pagingFrom, Integer pagingSize) {
        if (!userRepository.existsById(userId)) {
            throw new ElementNotFoundException("User " + userId + " does not exist");
        }
        List<EventShortDto> events = eventRepository.findAllByInitiatorId(userId, PageRequest.of(pagingFrom, pagingSize))
                .stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
        List<String> listOfUris = events.stream()
                .map(EventShortDto::getId)
                .map(Object::toString)
                .map(s -> "/events/" + s)
                .collect(Collectors.toList());

        Object bodyWithViews = statClient.getEventViews(listOfUris).getBody();

        return events.stream()
                .peek(event -> {
                    if (bodyWithViews instanceof LinkedHashMap) {
                        event.setViews(Long.parseLong(((LinkedHashMap<?, ?>) bodyWithViews).get(event.getId()
                                .toString()).toString()));
                    }
                })
                .collect(Collectors.toList());
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

    // TODO: ADD CONFIRMED REQUESTS COUNT
    public List<EventFullDto> getFullEvents(Set<Long> userIds, List<StatusEnum> states, List<Long> categories,
                                            LocalDateTime fromDate, LocalDateTime toDate, Integer pagingFrom,
                                            Integer pagingSize) {
        List<EventFullDto> events = eventRepository.getFullEventsFiltered(
                        userIds, states, categories, fromDate, toDate, PageRequest.of(pagingFrom, pagingSize))
                .stream()
                .map(EventMapper::toEventDto)
                .collect(Collectors.toList());

        List<String> listOfUris = events.stream()
                .map(EventFullDto::getId)
                .map(Object::toString)
                .map(s -> "/events/" + s)
                .collect(Collectors.toList());

        Object bodyWithViews = statClient.getEventViews(listOfUris).getBody();

        return events.stream()
                .peek(event -> {
                    if (bodyWithViews instanceof LinkedHashMap) {
                        event.setViews(Long.parseLong(((LinkedHashMap<?, ?>) bodyWithViews).get(event.getId()
                                .toString()).toString()));
                    }
                })
                .collect(Collectors.toList());
    }

    private <T extends EventShortDto> List<T> fillViews(List<T> events) {
        List<String> listOfUris = events.stream()
                .map(T::getId)
                .map(Object::toString)
                .map(s -> "/events/" + s)
                .collect(Collectors.toList());

        Object bodyWithViews = statClient.getEventViews(listOfUris).getBody();

        return events.stream()
                .peek(event -> {
                    if (bodyWithViews instanceof LinkedHashMap) {
                        event.setViews(Long.parseLong(((LinkedHashMap<?, ?>) bodyWithViews).get(event.getId()
                                .toString()).toString()));
                    }
                })
                .collect(Collectors.toList());
    }
}
