package ru.practicum.mainService.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.StatResponseDto;
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

@Slf4j
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
                        userIds, states, categories, fromDate, toDate, PageRequest.of(pagingFrom, pagingSize))
                .stream()
                .map(EventMapper::toEventDto)
                .collect(Collectors.toList());
        Object body = statClient.getStats(fromDate.toString(), toDate.toString(), false, events.stream()
                .map(EventFullDto::getId)
                .map(Object::toString)
                        .map(s -> {
                            System.out.println("the map content " + s); return s;
                        })
                .map(s -> "/events/" + s)
                .collect(Collectors.toList())).getBody();
        if (body instanceof String) {
            String bodyString = (String) body;
            System.out.println(bodyString);
            System.out.println("got string");
        } else if (body instanceof byte[]) {
            byte[] bodyBytes = (byte[]) body;
            System.out.println(new String(bodyBytes));
            System.out.println("got bites");
        } else {
            System.out.println("else happened");
            // Handle other types of body content as needed
        }

        log.warn(events.stream()
                .map(EventFullDto::getId)
                .map(Object::toString)
                .map(s -> "/events/" + s)
                .collect(Collectors.toList()).toString());

        return null;
    }
}
