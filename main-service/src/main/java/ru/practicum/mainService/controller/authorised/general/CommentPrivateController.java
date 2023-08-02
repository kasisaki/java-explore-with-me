package ru.practicum.mainService.controller.authorised.general;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainService.dto.request.ParticipationRequestDto;
import ru.practicum.mainService.service.RequestService;

import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events/{eventId}/comments")
public class CommentPrivateController {
    private final RequestService requestService;

    @GetMapping
    public ResponseEntity<List<ParticipationRequestDto>> findAllCommentsByEventId(@PathVariable @Positive Long userId) {
        log.info("Find all participation requests of User with id={}", userId);
        return new ResponseEntity<>(requestService.findAllByUserId(userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> createComment(@PathVariable @Positive Long userId,
                                                                 @RequestParam @Positive Long eventId) {
        log.info("Create comment from user with id={}, for event with id={}", userId, eventId);
        return new ResponseEntity<>(requestService.createRequest(userId, eventId), HttpStatus.CREATED);
    }

    @PatchMapping("/{commentId}/cancel")
    public ResponseEntity<ParticipationRequestDto> updateComment(@PathVariable @Positive Long userId,
                                                                 @PathVariable @Positive Long commentId) {
        log.info("User with id={} requested cancelation of request with id={}", userId, commentId);
        return new ResponseEntity<>(requestService.updateRequest(userId, commentId), HttpStatus.OK);
    }
}
