package ru.practicum.mainService.controller.authorised.general;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainService.dto.event.EventFullDto;
import ru.practicum.mainService.dto.event.EventShortDto;
import ru.practicum.mainService.dto.event.NewEventDto;
import ru.practicum.mainService.dto.event.UpdateEventUserRequest;
import ru.practicum.mainService.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.mainService.dto.request.EventRequestStatusUpdateResult;
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
    public ResponseEntity<List<EventShortDto>> getEvents(@PathVariable Long userId,
                                                         @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                         @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("Get events for user with id: {}, from: {}, size: {}", userId, from, size);
        return ResponseEntity.status(200).body(eventService.getShortEvents(userId, from, size));
    }

    @PostMapping
    public ResponseEntity<List<EventFullDto>> addEvent(@Valid @RequestBody NewEventDto createEventDto,
                                                       @PathVariable Long userId,
                                                       @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                       @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("Add event for user with body: {}, id: {}, from: {}, size: {}", createEventDto, userId, from, size);
        return ResponseEntity.status(200).body(null);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEvent(@PathVariable("userId") Long userId,
                                                 @PathVariable("eventId") Long eventId) {
        log.info("Get event with id: {} for user with id: {}", eventId, userId);
        return ResponseEntity.status(200).body(null);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(@Valid @RequestBody UpdateEventUserRequest updateEventUserRequestDto,
                                                    @PathVariable("userId") Long userId,
                                                    @PathVariable("eventId") Long eventId) {
        log.info("Update event with id: {} for user with id: {} with body: {}", eventId, userId, updateEventUserRequestDto);
        return ResponseEntity.status(200).body(null);
    }

    @GetMapping("{eventId}/requests")
    public ResponseEntity<ParticipationRequestDto> getEventRequest(@PathVariable("userId") Long userId,
                                                                   @PathVariable("eventId") Long eventId) {
        log.info("Get event request with id: {} for user with id: {}", eventId, userId);
        return ResponseEntity.status(200).body(null);
    }

    @PatchMapping("{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> updateEventRequest(@Valid @RequestBody EventRequestStatusUpdateRequest dto,
                                                                             @PathVariable("userId") Long userId,
                                                                             @PathVariable("eventId") Long eventId) {
        log.info("Update event request with id: {} for user with id: {} with body: {}", eventId, userId, dto);
        return ResponseEntity.status(200).body(null);
    }
}
