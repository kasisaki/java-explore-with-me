package ru.practicum.mainService.controller.unauthorised;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainService.dto.comment.CommentShortDto;
import ru.practicum.mainService.service.CommentService;

import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events/{eventId}/comments")
public class CommentPublicController {
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentShortDto>> findAllCommentsByEventId(@PathVariable @Positive Long eventId) {
        log.info("Find all comments of Event with id={}", eventId);
        return new ResponseEntity<>(commentService.findAllByEventId(eventId), HttpStatus.OK);
    }
}
