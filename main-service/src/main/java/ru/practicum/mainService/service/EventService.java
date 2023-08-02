package ru.practicum.mainService.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.StatHitDto;
import ru.practicum.mainService.dto.event.*;
import ru.practicum.mainService.dto.location.LocationDto;
import ru.practicum.mainService.dto.request.ParticipationRequestDto;
import ru.practicum.mainService.dto.user.UserShortDto;
import ru.practicum.mainService.mappers.EventMapper;
import ru.practicum.mainService.mappers.RequestMapper;
import ru.practicum.mainService.models.Event;
import ru.practicum.mainService.models.Location;
import ru.practicum.mainService.models.Request;
import ru.practicum.mainService.repositories.EventRepository;
import ru.practicum.mainService.repositories.LocationRepository;
import ru.practicum.mainService.repositories.RequestRepository;
import ru.practicum.mainService.repositories.UserRepository;
import ru.practicum.mainService.utils.enums.EventStatusEnum;
import ru.practicum.mainService.utils.enums.SortEventsEnum;
import ru.practicum.mainService.utils.exceptions.ConflictException;
import ru.practicum.mainService.utils.exceptions.ElementNotFoundException;
import ru.practicum.mainService.utils.exceptions.IllegalStatusException;
import ru.practicum.statsClient.StatClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.mainService.mappers.EventMapper.*;
import static ru.practicum.mainService.mappers.LocationMapper.dtoToLocation;
import static ru.practicum.mainService.mappers.UserMapper.userToShortDto;
import static ru.practicum.mainService.utils.enums.EventStatusEnum.CANCELED;
import static ru.practicum.mainService.utils.enums.EventStatusEnum.PENDING;
import static ru.practicum.mainService.utils.enums.RequestStatusEnum.CONFIRMED;
import static ru.practicum.mainService.utils.enums.RequestStatusEnum.REJECTED;
import static ru.practicum.mainService.utils.enums.SortEventsEnum.EVENT_DATE;
import static ru.practicum.mainService.utils.enums.SortEventsEnum.VIEWS;
import static ru.practicum.utils.Constants.DATE_PATTERN;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final StatClient statClient;


    //Public service

    // TODO информация о каждом событии должна включать в себя количество просмотров и количество уже одобренных заявок на участие
    public List<EventShortDto> getShortEventsFilter(String text, List<Long> categoriesId, Boolean paid,
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
        Pageable pageable;

        if (sort.equals(VIEWS)) {
            pageable = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "views"));
        } else if (sort.equals(EVENT_DATE)) {
            pageable = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "eventDate"));
        } else {
            throw new IllegalStatusException("Unknown sort parameter");
        }
        log.error(rangeStart.toString());
        log.error("RANGE END");
        log.error(rangeEnd.toString());
        List<Event> events;
        if (onlyAvailable) {
            events = eventRepository.
                    getShortEventsAvailableFilter(pageable, text, categoriesId, paid, rangeStart, rangeEnd)
                    .toList();
        } else {
            events = eventRepository
                    .getShortEventsFilter(pageable, text, categoriesId, paid, rangeStart, rangeEnd)
                    .toList();
        }

        return fillViewsForListAndReturn(events
                .stream()
                .map(EventMapper::eventToShortDto)
                .map(this::setNumberOfConfirmedRequest)
                .collect(Collectors.toList()));
//        List<Event> events;
//        if (onlyAvailable) {
//            events = eventRepository.getShortEventsFilter(text, categoriesId, paid, rangeStart, rangeEnd);
//        } else {
//            events = eventRepository.getShortEventsFilter(text, categoriesId, paid, rangeStart, rangeEnd);
//        }
//
//        List<EventShortDto> eventDtos = fillViewsForListAndReturn(events
//                .stream()
//                .map(EventMapper::eventToShortDto)
//                .map(this::setNumberOfConfirmedRequest)
//                .collect(Collectors.toList()));
//
//        if (sort == null) {
//            return eventDtos.stream()
//                    .skip(from)
//                    .limit(size)
//                    .collect(Collectors.toList());
//        } else if (sort.equals(VIEWS)) {
//            return eventDtos.stream()
//                    .sorted(Comparator.comparing(EventShortDto::getViews))
//                    .skip(from)
//                    .limit(size)
//                    .collect(Collectors.toList());
//        }
//        return eventDtos.stream()
//                .sorted(Comparator.comparing(EventShortDto::getEventDate))
//                .skip(from)
//                .limit(size)
//                .collect(Collectors.toList());
    }

    public EventFullDto getEvent(Long eventId, HttpServletRequest request) {
        statClient.appendStats(new StatHitDto(request.getRequestURI(),
                request.getRemoteAddr(),
                "ewm-events-service",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_PATTERN))));

        return setViewsOfEventAndReturn(
                setNumberOfConfirmedRequest(
                        eventToFullEventDto(
                                eventRepository.findById(eventId).orElseThrow()
                        )
                )
        );
    }


    // User event services

    public List<EventShortDto> getShortEventsOfUser(Long userId, Integer pagingFrom, Integer pagingSize) {
        checkUser(userId);
        List<EventShortDto> events = eventRepository.findAllByInitiatorId(userId, PageRequest.of(pagingFrom, pagingSize))
                .stream()
                .map(EventMapper::eventToShortDto)
                .map(this::setNumberOfConfirmedRequest)
                .collect(Collectors.toList());
        return fillViewsForListAndReturn(events);
    }

    public EventFullDto getFullEventOfUser(Long userId, Long eventId) {
        checkUser(userId);
        return setViewsOfEventAndReturn(
                setNumberOfConfirmedRequest(
                        eventToFullEventDto(
                                eventRepository.findFirstByIdAndInitiatorId(eventId, userId).orElseThrow()
                        )
                )
        );
    }

    @Transactional
    public EventFullDto createEventByUser(Long userId, NewEventDto createEventDto) {
        UserShortDto user = userToShortDto(userRepository.findById(userId).orElseThrow());
        if (createEventDto.getEventDate().minusHours(2).isBefore(LocalDateTime.now())) {
            throw new ConflictException("Дата и время на которые намечено событие не может быть раньше, " +
                    "чем через два часа от текущего момента ");
        }
        return setViewsOfEventAndReturn(
                setNumberOfConfirmedRequest(
                        eventToFullEventDto(
                                eventRepository.save(
                                        createDtoToEvent(
                                                createEventDto, saveLocation(createEventDto.getLocation()), user)
                                )
                        )
                )
        );
    }

    @Transactional
    public EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest updateDto) {
        checkUser(userId);
        Event event = eventRepository.findById(eventId).orElseThrow();
        if (!event.getState().equals(CANCELED) && !event.getState().equals(PENDING)) {
            throw new ConflictException("Изменить можно только отмененные события " +
                    "или события в состоянии ожидания модерации");
        }
        if (event.getEventDate().minusHours(2).isBefore(LocalDateTime.now())) {
            throw new ConflictException("Дата и время на которые намечено событие не может быть раньше, " +
                    "чем через два часа от текущего момента ");
        }
        return setViewsOfEventAndReturn(
                setNumberOfConfirmedRequest(
                        eventToFullEventDto(
                                eventRepository.save(
                                        updateDtoToEvent(
                                                updateDto, event, updateDto.getLocation() == null
                                                        ? event.getLocation()
                                                        : saveLocation(updateDto.getLocation()))
                                )
                        )
                )
        );
    }

    public List<ParticipationRequestDto> getEventRequestByUser(Long userId, Long eventId) {
        checkUser(userId);
        checkEvent(eventId);
        return requestRepository.findAllByEventId(eventId).stream()
                .map(RequestMapper::requestToRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventRequestStatusUpdateResult updateEventRequestByUser(Long userId, Long eventId,
                                                                   EventRequestStatusUpdateRequest updateRequest) {
        checkUser(userId);
        if (!eventRepository.findById(eventId).orElseThrow().getInitiator().getId().equals(userId)) {
            throw new ConflictException("Event with id " + eventId + " is not initialized by user " + userId);
        }
        Integer participantLimit = eventRepository.findById(eventId).orElseThrow().getParticipantLimit();
        Integer acceptedRequestCount = requestRepository.countByEventIdAndStatus(eventId, CONFIRMED);
        if (participantLimit < acceptedRequestCount + updateRequest.getRequestIds().size()) {
            throw new ConflictException("Запрос превысит количество лимит участников");
        }
        List<Request> requests = requestRepository.findAllByIdIn(updateRequest.getRequestIds())
                .stream()
                .peek(request -> request.setStatus(updateRequest.getStatus()))
                .collect(Collectors.toList());

        requestRepository.saveAll(requests);
        List<ParticipationRequestDto> confirmedRequests =
                requestRepository.findAllByEventIdAndStatus(eventId, CONFIRMED)
                        .stream()
                        .map(RequestMapper::requestToRequestDto)
                        .collect(Collectors.toList());
        List<ParticipationRequestDto> rejectedRequests =
                requestRepository.findAllByEventIdAndStatus(eventId, REJECTED).stream()
                        .map(RequestMapper::requestToRequestDto)
                        .collect(Collectors.toList());
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    // Admin services
    @Transactional
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new ElementNotFoundException("Event with id " + eventId + " does not exist")
        );
        log.error("Event update by Admin event from repo + {}", event.toString());
        Event updatedEvent = eventRepository.save(
                updateDtoToEvent(updateDto, event,
                        updateDto.getLocation() == null ? event.getLocation() : saveLocation(updateDto.getLocation())));

        log.error("Event update by Admin event updated + {}", updatedEvent);

        return setViewsOfEventAndReturn(
                setNumberOfConfirmedRequest(
                        eventToFullEventDto(updatedEvent
                        )
                )
        );
    }

    public List<EventFullDto> getFullEvents(Set<Long> userIds, List<EventStatusEnum> states, List<Long> categories,
                                            LocalDateTime fromDate, LocalDateTime toDate, Integer pagingFrom,
                                            Integer pagingSize) {
        List<EventFullDto> events = eventRepository.getFullEventsFiltered(
                        userIds, states, categories, fromDate, toDate, PageRequest.of(pagingFrom, pagingSize))
                .stream()
                .map(EventMapper::eventToFullEventDto)
                .map(this::setNumberOfConfirmedRequest)
                .collect(Collectors.toList());

        return fillViewsForListAndReturn(events);
    }


    // Private methods

    private <T extends EventShortDto> List<T> fillViewsForListAndReturn(List<T> events) {
        List<String> listOfUris = events.stream()
                .map(T::getId)
                .map(Object::toString)
                .map(s -> "/events/" + s)
                .collect(Collectors.toList());

        Object bodyWithViews = statClient.getHitsOfUris(listOfUris).getBody();

        return events.stream()
                .peek(event -> {
                    if (bodyWithViews instanceof LinkedHashMap && event.getId() != null) {
                        Object views = ((LinkedHashMap<?, ?>) bodyWithViews).get(event.getId().toString());
                        if (views != null) {
                            event.setViews(Long.parseLong(views.toString()));
                        } else {
                            event.setViews(0L);
                        }
                    }
                })
                .collect(Collectors.toList());
    }

    private <T extends EventShortDto> T setViewsOfEventAndReturn(T event) {
        Object bodyWithViews = statClient.getHitsOfUri("/events/" + event.getId()).getBody();
        Object views;
        if (bodyWithViews instanceof LinkedHashMap && event.getId() != null) {
            views = ((LinkedHashMap<?, ?>) bodyWithViews).get(event.getId().toString());
            if (views != null) {
                event.setViews(Long.parseLong(views.toString()));
                return event;
            }
        }
        event.setViews(0L);
        return event;
    }

    private <T extends EventShortDto> T setNumberOfConfirmedRequest(T event) {
        event.setConfirmedRequests(
                requestRepository.countByEventIdAndStatus(event.getId(), CONFIRMED)
        );
        return event;
    }

    private Location saveLocation(LocationDto locationDto) {
        if (!locationRepository.existsByLatAndLon(locationDto.getLat(), locationDto.getLon())) {
            return locationRepository.saveAndFlush(dtoToLocation(locationDto));
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
