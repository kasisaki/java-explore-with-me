package ru.practicum.mainService.controller.unauthorised;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainService.dto.comment.CommentShortDto;
import ru.practicum.mainService.service.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events/{eventId}/comments")
public class CommentPublicController {
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentShortDto>> findAllCommentsByEventId(
            @PathVariable @Positive Long eventId,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @PositiveOrZero Integer size) {

        log.info("Find all comments of Event with id={}", eventId);
        return new ResponseEntity<>(commentService.findAllByEventId(eventId, from, size), HttpStatus.OK);
    }
}
