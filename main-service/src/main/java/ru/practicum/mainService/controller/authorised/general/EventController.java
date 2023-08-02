package ru.practicum.mainService.controller.authorised.general;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainService.dto.event.*;
import ru.practicum.mainService.dto.request.ParticipationRequestDto;
import ru.practicum.mainService.service.EventService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class EventController {
    private final EventService eventService;


    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEventsOfUser(@PathVariable Long userId,
                                                               @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                               @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("Get events for user with id: {}, from: {}, size: {}", userId, from, size);
        return ResponseEntity.status(200).body(eventService.getShortEventsOfUser(userId, from, size));
    }

    @PostMapping
    public ResponseEntity<EventFullDto> createEvent(@RequestBody @Valid NewEventDto createEventDto,
                                                    @PathVariable Long userId) {
        log.info("Add event for user with body: {}, id: {}", createEventDto, userId);
        return new ResponseEntity<>(eventService.createEventByUser(userId, createEventDto), HttpStatus.CREATED);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getFullEventOfUser(@PathVariable("userId") Long userId,
                                                           @PathVariable("eventId") Long eventId) {
        log.info("Get event with id: {} for user with id: {}", eventId, userId);
        return new ResponseEntity<>(eventService.getFullEventOfUser(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateUserEvent(@Valid @RequestBody UpdateEventUserRequest updateDto,
                                                        @PathVariable("userId") Long userId,
                                                        @PathVariable("eventId") Long eventId) {
        log.info("Update event with id: {} for user with id: {} with body: {}", eventId, userId, updateDto);
        return new ResponseEntity<>(eventService.updateEventByUser(userId, eventId, updateDto), HttpStatus.OK);
    }

    // TODO getEventRequest
    @GetMapping("{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getUserEventRequests(@PathVariable("userId") Long userId,
                                                                              @PathVariable("eventId") Long eventId) {
        log.info("Get event request with id: {} for user with id: {}", eventId, userId);
        return new ResponseEntity<>(eventService.getEventRequestByUser(userId, eventId), HttpStatus.OK);
    }

    // TODO updateEventRequest
    @PatchMapping("{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> updateUserEventParticipationRequest(
            @Valid @RequestBody EventRequestStatusUpdateRequest updateRequestDto,
            @PathVariable("userId") Long userId,
            @PathVariable("eventId") Long eventId) {
        log.info("Update event request with id: {} for user with id: {} with body: {}",
                eventId, userId, updateRequestDto);
        return new ResponseEntity<>(eventService.updateEventRequestByUser(userId, eventId, updateRequestDto), HttpStatus.OK);
    }
}
