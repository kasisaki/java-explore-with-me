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
import ru.practicum.mainService.utils.exceptions.BadRequestException;
import ru.practicum.mainService.utils.exceptions.ConflictException;
import ru.practicum.mainService.utils.exceptions.ElementNotFoundException;
import ru.practicum.statsClient.StatClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.mainService.mappers.EventMapper.*;
import static ru.practicum.mainService.mappers.LocationMapper.dtoToLocation;
import static ru.practicum.mainService.mappers.UserMapper.userToShortDto;
import static ru.practicum.mainService.utils.DateUtils.getDatePatterned;
import static ru.practicum.mainService.utils.enums.EventStatusEnum.*;
import static ru.practicum.mainService.utils.enums.RequestStatusEnum.CONFIRMED;
import static ru.practicum.mainService.utils.enums.RequestStatusEnum.REJECTED;
import static ru.practicum.mainService.utils.enums.SortEventsEnum.COMMENTS;
import static ru.practicum.mainService.utils.enums.SortEventsEnum.VIEWS;
import static ru.practicum.utils.Constants.DATE_PATTERN;
import static ru.practicum.utils.Constants.END_DATE;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final StatClient statClient;


    // Public service
    public List<EventShortDto> getShortEventsFilter(String app, String text, List<Long> categoriesId, Boolean paid,
                                                    LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                    Boolean onlyAvailable, SortEventsEnum sort, int from, int size,
                                                    HttpServletRequest request) {
        statClient.appendStats(new StatHitDto(app,
                request.getRequestURI(),
                request.getRemoteAddr(),
                getDatePatterned()));

        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
        }
        if (rangeEnd == null) {
            rangeEnd = LocalDateTime.parse(END_DATE, DateTimeFormatter.ofPattern(DATE_PATTERN));
        }
        if (rangeStart.isAfter(rangeEnd)) {
            throw new BadRequestException("Invalid range");
        }

        Pageable pageable = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "eventDate"));

        if (sort != null && sort.equals(VIEWS)) {
            pageable = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "views"));
        } else if (sort != null && sort.equals(COMMENTS)) {
            pageable = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "comments"));
        }

        List<Event> events;
        if (onlyAvailable) {
            events = eventRepository
                    .getShortEventsAvailableFilter(pageable, text, categoriesId, paid, rangeStart, rangeEnd)
                    .toList();
        } else {
            events = eventRepository
                    .getShortEventsFilter(pageable, text, categoriesId, paid, rangeStart, rangeEnd)
                    .toList();
        }

        return setViewsForListOfShortDto(events
                .stream()
                .map(EventMapper::eventToShortDto)
                .map(this::setRequestsForShortDto)
                .collect(Collectors.toList()));
    }


    public EventFullDto getEvent(String app, Long eventId, HttpServletRequest request) {
        statClient.appendStats(new StatHitDto(app,
                request.getRequestURI(),
                request.getRemoteAddr(),
                getDatePatterned()));

        Event event = eventRepository.findById(eventId).orElseThrow();

        if (!event.getState().equals(PUBLISHED)) {
            throw new ElementNotFoundException("Event with id " + event + " is not found");
        }
        List<EventFullDto> events = setViewsForListOfFullDto(List.of(
                setRequestsForFull(
                        eventToFullEventDto(event)
                )
        ));

        return events.get(0);
    }

    // User event services
    public List<EventShortDto> getShortEventsOfUser(Long userId, Integer pagingFrom, Integer pagingSize) {
        checkUser(userId);
        List<EventShortDto> events = eventRepository.findAllByInitiatorId(userId, PageRequest.of(pagingFrom, pagingSize))
                .stream()
                .map(EventMapper::eventToShortDto)
                .map(this::setRequestsForShortDto)
                .collect(Collectors.toList());
        return setViewsForListOfShortDto(events);
    }

    public EventFullDto getFullEventOfUser(Long userId, Long eventId) {
        checkUser(userId);
        List<EventFullDto> events = setViewsForListOfFullDto(List.of(
                setRequestsForFull(
                        eventToFullEventDto(
                                eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow()
                        )
                )
        ));
        return events.get(0);
    }

    @Transactional
    public EventFullDto createEventByUser(Long userId, NewEventDto createEventDto) {
        UserShortDto user = userToShortDto(userRepository.findById(userId).orElseThrow());
        if (createEventDto.getEventDate().minusHours(2).isBefore(LocalDateTime.now())) {
            throw new BadRequestException("The date and time for which the event is scheduled cannot be " +
                    "earlier than two hours from the current moment");
        }
        List<EventFullDto> events = setViewsForListOfFullDto(List.of(
                setRequestsForFull(
                        eventToFullEventDto(
                                eventRepository.save(
                                        createDtoToEvent(
                                                createEventDto, saveLocation(createEventDto.getLocation()), user)
                                )
                        )
                )
        ));
        return events.get(0);
    }

    @Transactional
    public EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest updateDto) {
        checkUser(userId);
        Event event = eventRepository.findById(eventId).orElseThrow();
        if (!event.getState().equals(CANCELED) && !event.getState().equals(PENDING)) {
            throw new ConflictException("Only uploaded events or events pending moderation can be edited");
        }
        if (event.getEventDate().minusHours(2).isBefore(LocalDateTime.now())) {
            throw new ConflictException("The date and time for which the event is scheduled cannot be earlier " +
                    "than two hours from the current moment");
        }
        List<EventFullDto> events = setViewsForListOfFullDto(List.of(
                setRequestsForFull(
                        eventToFullEventDto(
                                eventRepository.save(
                                        updateDtoToEvent(
                                                updateDto, event, updateDto.getLocation() == null
                                                        ? event.getLocation()
                                                        : saveLocation(updateDto.getLocation()))
                                )
                        )
                )
        ));
        return events.get(0);
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
            throw new ConflictException("The request will exceed the number of participants limit");
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

        List<EventFullDto> events = setViewsForListOfFullDto(List.of(
                setRequestsForFull(
                        eventToFullEventDto(updatedEvent
                        )
                )
        ));
        return events.get(0);
    }

    public List<EventFullDto> getFullEvents(Set<Long> userIds, List<EventStatusEnum> states, List<Long> categories,
                                            LocalDateTime fromDate, LocalDateTime toDate, Integer pagingFrom,
                                            Integer pagingSize) {
        List<EventFullDto> events = eventRepository.getFullEventsFiltered(
                        userIds, states, categories, fromDate, toDate, PageRequest.of(pagingFrom, pagingSize))
                .stream()
                .map(EventMapper::eventToFullEventDto)
                .map(this::setRequestsForFull)
                .collect(Collectors.toList());

        return setViewsForListOfFullDto(events);

    }

    // Private methods
    private List<EventFullDto> setViewsForListOfFullDto(List<EventFullDto> events) {
        List<Long> eventIds = events
                .stream()
                .map(EventFullDto::getId)
                .collect(Collectors.toList());

        Map<Long, Long> mapOfViews = getViewsForList(eventIds);


        return events
                .stream()
                .peek(event -> event.setViews(mapOfViews.get(event.getId())))
                .collect(Collectors.toList());
    }

    private List<EventShortDto> setViewsForListOfShortDto(List<EventShortDto> events) {
        List<Long> eventIds = events
                .stream()
                .map(EventShortDto::getId)
                .collect(Collectors.toList());

        Map<Long, Long> mapOfViews = getViewsForList(eventIds);

        return events
                .stream()
                .peek(event -> event.setViews(mapOfViews.get(event.getId())))
                .collect(Collectors.toList());
    }

    private Map<Long, Long> getViewsForList(List<Long> eventIds) {
        List<String> listOfUris = eventIds.stream()
                .map(Object::toString)
                .map(s -> "/events/" + s)
                .collect(Collectors.toList());


        Object bodyWithViews = statClient.getHitsOfUris(listOfUris).getBody();
        Map<Long, Long> mapOfViews = new HashMap<>();

        eventIds.forEach(id -> {
            if (bodyWithViews instanceof LinkedHashMap && !eventIds.isEmpty()) {
                Object views = ((LinkedHashMap<?, ?>) bodyWithViews).get(id.toString());
                if (views != null) {
                    mapOfViews.put(id, Long.parseLong(views.toString()));
                } else {
                    mapOfViews.put(id, 0L);
                }
            }
        });
        return mapOfViews;
    }

    private EventFullDto setRequestsForFull(EventFullDto event) {

        event.setConfirmedRequests(
                requestRepository.countByEventIdAndStatus(event.getId(), CONFIRMED)
        );
        return event;
    }

    private EventShortDto setRequestsForShortDto(EventShortDto event) {
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
