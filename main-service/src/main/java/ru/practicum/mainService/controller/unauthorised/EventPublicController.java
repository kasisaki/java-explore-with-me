package ru.practicum.mainService.controller.unauthorised;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainService.dto.event.EventFullDto;
import ru.practicum.mainService.dto.event.EventShortDto;
import ru.practicum.mainService.service.EventService;
import ru.practicum.mainService.utils.enums.SortEventsEnum;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.utils.Constants.DATE_PATTERN;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("events")
public class EventPublicController {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(name = "categories", required = false) List<Long> categoriesId,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime rangeStart,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) SortEventsEnum sort,
            @PositiveOrZero @RequestParam(defaultValue = "0") int from,
            @Positive @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        log.info("Get events with \ntext: {}, \ncategories: {}, \npaid: {}, \nrangeStart: {}, \nrangeEnd: {}, " +
                        "\nonlyAvailable: {}, \nsort: {}, \nfrom: {}, \nsize: {}", text, categoriesId, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size);

        return ResponseEntity.status(200).body(eventService.getShortEventsFilter(text, categoriesId, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventFullDto> getEvent(
            @PathVariable("id") @Positive Long eventId, HttpServletRequest request) {
        log.info("Get event with id: {}, request: {}", eventId, request);
        return ResponseEntity.status(200).body(eventService.getEvent(eventId, request));
    }
}
