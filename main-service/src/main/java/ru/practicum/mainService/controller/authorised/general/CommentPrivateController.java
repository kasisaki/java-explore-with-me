package ru.practicum.mainService.controller.authorised.general;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainService.dto.comment.CommentDto;
import ru.practicum.mainService.dto.comment.NewCommentDto;
import ru.practicum.mainService.service.CommentService;

import javax.validation.constraints.Positive;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events/{eventId}/users/{userId}/comments")
public class CommentPrivateController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDto> createComment(@RequestBody NewCommentDto dto,
                                                    @PathVariable @Positive Long userId,
                                                    @PathVariable @Positive Long eventId) {
        log.info("Create comment from user with id={}, for event with id={}", userId, eventId);
        return new ResponseEntity<>(commentService.createComment(userId, eventId, dto), HttpStatus.CREATED);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> findByCommentId(@PathVariable @Positive Long userId,
                                                      @PathVariable @Positive Long eventId,
                                                      @PathVariable @Positive Long commentId) {
        log.info("Find all participation requests of User with id={}", userId);
        return new ResponseEntity<>(commentService.findByCommentId(userId, eventId, commentId), HttpStatus.OK);
    }

    @PatchMapping("/{commentId}/update")
    public ResponseEntity<CommentDto> updateComment(@RequestBody NewCommentDto dto,
                                                    @PathVariable @Positive Long userId,
                                                    @PathVariable @Positive Long eventId,
                                                    @PathVariable @Positive Long commentId) {
        log.info("User with id={} requested deletion of request with id={}", userId, commentId);
        return new ResponseEntity<>(commentService.updateComment(userId, eventId, commentId, dto), HttpStatus.OK);
    }
}
