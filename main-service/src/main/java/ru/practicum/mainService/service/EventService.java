package ru.practicum.mainService.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.StatHitDto;
import ru.practicum.mainService.dto.event.*;
import ru.practicum.mainService.dto.location.LocationDto;
import ru.practicum.mainService.mappers.EventMapper;
import ru.practicum.mainService.models.Event;
import ru.practicum.mainService.models.Location;
import ru.practicum.mainService.repositories.EventRepository;
import ru.practicum.mainService.repositories.LocationRepository;
import ru.practicum.mainService.repositories.UserRepository;
import ru.practicum.mainService.utils.enums.SortEventsEnum;
import ru.practicum.mainService.utils.enums.StatusEnum;
import ru.practicum.mainService.utils.exceptions.ConflictException;
import ru.practicum.mainService.utils.exceptions.ElementNotFoundException;
import ru.practicum.statsClient.StatClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.mainService.mappers.EventMapper.*;
import static ru.practicum.mainService.mappers.LocationMapper.dtoToLocation;
import static ru.practicum.mainService.utils.enums.SortEventsEnum.VIEWS;
import static ru.practicum.mainService.utils.enums.StateActionEnum.PUBLISH_EVENT;
import static ru.practicum.mainService.utils.enums.StateActionEnum.REJECT_EVENT;
import static ru.practicum.mainService.utils.enums.StatusEnum.*;
import static ru.practicum.utils.Constants.DATE_PATTERN;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final StatClient statClient;


    //Public service

    // TODO информация о каждом событии должна включать в себя количество просмотров и количество уже одобренных заявок на участие
    public List<EventShortDto> getShortEventsFilter(String text, List<Integer> categoriesId, Boolean paid,
                                                    LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                    Boolean onlyAvailable, SortEventsEnum sort, int from, int size,
                                                    HttpServletRequest request) {
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
        }
        statClient.appendStats(new StatHitDto(request.getRequestURI(),
                request.getRemoteAddr(),
                "ewm-events-service",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_PATTERN))));

        List<EventShortDto> events = fillViewsForList(eventRepository.getShortEventsFilter(text, categoriesId, paid, rangeStart, rangeEnd, onlyAvailable)
                .stream()
                .map(EventMapper::eventToShortDto).collect(Collectors.toList()));

        if (sort == null) {
            return events.stream()
                    .skip(from)
                    .limit(size)
                    .collect(Collectors.toList());
        } else if (sort.equals(VIEWS)) {
            return events.stream()
                    .sorted(Comparator.comparing(EventShortDto::getViews))
                    .skip(from)
                    .limit(size)
                    .collect(Collectors.toList());
        }
        return events.stream()
                .sorted(Comparator.comparing(EventShortDto::getEventDate))
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());
    }

    public EventFullDto getEvent(Long eventId, HttpServletRequest request) {
        statClient.appendStats(new StatHitDto(request.getRequestURI(),
                request.getRemoteAddr(),
                "ewm-events-service",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_PATTERN))));

        return fillViewsForList(List.of(eventToFullEventDto(eventRepository.findById(eventId).orElseThrow()))).get(0);
    }


    // User event services

    public List<EventShortDto> getShortEventsOfUser(Long userId, Integer pagingFrom, Integer pagingSize) {
        checkUser(userId);
        List<EventShortDto> events = eventRepository.findAllByInitiatorId(userId, PageRequest.of(pagingFrom, pagingSize))
                .stream()
                .map(EventMapper::eventToShortDto)
                .collect(Collectors.toList());
        return fillViewsForList(events);
    }

    public EventFullDto getFullEventOfUser(Long userId, Long eventId) {
        checkUser(userId);
        return fillViewsForList(List.of(eventToFullEventDto(
                eventRepository.findFirstByIdAndInitiatorId(eventId, userId).orElseThrow()))).get(0);
    }

    public EventFullDto createEvent(Long userId, NewEventDto createEventDto) {
        checkUser(userId);
        if (createEventDto.getEventDate().minusHours(2).isBefore(LocalDateTime.now())) {
            throw new ConflictException("Дата и время на которые намечено событие не может быть раньше, " +
                    "чем через два часа от текущего момента ");
        }
        return fillViewsForList(List.of(eventToFullEventDto(eventRepository.save(
                createDtoToEvent(createEventDto, saveLocation(createEventDto.getLocation())))))).get(0);
    }

    public EventFullDto updateUserEvent(Long userId, Long eventId, UpdateEventUserRequest updateDto) {
        checkUser(userId);
        Event event = eventRepository.findById(eventId).orElseThrow();
        if (!event.getState().equals(CANCELED) || !event.getRequestModeration()) {
            throw new ConflictException("Изменить можно только отмененные события " +
                    "или события в состоянии ожидания модерации");
        }
        if (updateDto.getEventDate().minusHours(2).isBefore(LocalDateTime.now())) {
            throw new ConflictException("Дата и время на которые намечено событие не может быть раньше, " +
                    "чем через два часа от текущего момента ");
        }
        return fillViewsForList(List.of(eventToFullEventDto(eventRepository.save(
                updateAdminDtoToEvent(updateDto, event, saveLocation(updateDto.getLocation())))))).get(0);
    }

    // Admin services
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateDto) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        if (!event.getPublishedOn().isBefore(updateDto.getEventDate().minusHours(1))) {
            throw new ConflictException("Дата начала изменяемого события должна быть " +
                    "не ранее чем за час от даты публикации.");
        }
        if (updateDto.getStateAction().equals(PUBLISH_EVENT) && !event.getState().equals(PENDING)) {
            throw new ConflictException("Событие можно публиковать, только если оно в состоянии ожидания публикации");
        }
        if (updateDto.getStateAction().equals(REJECT_EVENT) && event.getState().equals(PUBLISHED)) {
            throw new ConflictException("Событие можно отклонить, только если оно еще не опубликовано");
        }
        return eventToFullEventDto(eventRepository.save(
                updateAdminDtoToEvent(updateDto, event,
                        updateDto.getLocation() == null ? event.getLocation() : saveLocation(updateDto.getLocation()))));
    }

    // TODO: ADD CONFIRMED REQUESTS COUNT
    public List<EventFullDto> getFullEvents(Set<Long> userIds, List<StatusEnum> states, List<Long> categories,
                                            LocalDateTime fromDate, LocalDateTime toDate, Integer pagingFrom,
                                            Integer pagingSize) {
        List<EventFullDto> events = eventRepository.getFullEventsFiltered(
                        userIds, states, categories, fromDate, toDate, PageRequest.of(pagingFrom, pagingSize))
                .stream()
                .map(EventMapper::eventToFullEventDto)
                .collect(Collectors.toList());

        return fillViewsForList(events);
    }


    // Private methods

    private <T extends EventShortDto> List<T> fillViewsForList(List<T> events) {
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

    private Location saveLocation(LocationDto locationDto) {
        if (!locationRepository.existsByLatAndLon(locationDto.getLat(), locationDto.getLon())) {
            return locationRepository.save(dtoToLocation(locationDto));
        }
        return locationRepository.findByLatAndLon(locationDto.getLat(), locationDto.getLon());
    }

    private void checkUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ElementNotFoundException("User " + userId + "does not exist");
        }
    }

    private void checkEvent(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new ElementNotFoundException("Event with id=" + eventId + " was not found");
        }
    }
}
