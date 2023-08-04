package ru.practicum.mainService.controller.unauthorised;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    public ResponseEntity<List<EventShortDto>> getEvents(@Value("${app.name}") final String app,
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

        log.info("Get events in {} with \ntext: {}, \ncategories: {}, \npaid: {}, \nrangeStart: {}, \nrangeEnd: {}, " +
                        "\nonlyAvailable: {}, \nsort: {}, \nfrom: {}, \nsize: {}", app, text, categoriesId, paid,
                rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        return ResponseEntity.status(200).body(eventService.getShortEventsFilter(app, text, categoriesId, paid,
                rangeStart, rangeEnd, onlyAvailable, sort, from, size, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventFullDto> getEvent(@Value("${app.name}") final String app,
                                                 @PathVariable("id") @Positive Long eventId, HttpServletRequest request) {
        log.info("Get event request with id: {}, request: {}", eventId, request);

        return ResponseEntity.status(200).body(eventService.getEvent(app, eventId, request));
    }
}
