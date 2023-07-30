package ru.practicum.mainService.controller.authorised.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainService.dto.event.EventFullDto;
import ru.practicum.mainService.dto.event.UpdateEventAdminRequest;
import ru.practicum.mainService.service.EventService;
import ru.practicum.mainService.utils.enums.StatusEnum;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static ru.practicum.utils.Constants.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventFullDto>> getEvents(
            @RequestParam(name = "users", required = false) Set<Long> users,
            @RequestParam(name = "states", required = false) List<StatusEnum> states,
            @RequestParam(name = "categories", required = false) List<Long> categories,
            @RequestParam(name = "rangeStart", required = false, defaultValue = START_DATE)
            @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime rangeStart,
            @RequestParam(name = "rangeEnd", required = false, defaultValue = END_DATE)
            @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime rangeEnd,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {

        log.info("Get events with \nusers: {}, \nstates: {}, \ncategories: {}, \nrangeStart: {}, \nrangeEnd: {}, " +
                "\nfrom: {}, \nsize: {}", users, states, categories, rangeStart, rangeEnd, from, size);

        return new ResponseEntity<>(
                eventService.getFullEvents(users, states, categories, rangeStart, rangeEnd, from, size), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(@PathVariable @NotNull Long eventId,
                                                    @RequestBody UpdateEventAdminRequest event) {
        log.info("Update event with \nid: {} and \ndata:", eventId);
        return new ResponseEntity<>(eventService.updateEvent(eventId, event), HttpStatus.OK);
    }
}