package ru.practicum.mainService.controller.authorised.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainService.dto.comment.CommentDto;
import ru.practicum.mainService.service.CommentService;

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
@RequestMapping("/admin/comments")
public class AdminCommentController {
    private final CommentService commentService;

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        log.debug("Admin requested deletion of comment with id {}", commentId);

        commentService.deleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> getCommentsFiltered(
            @RequestParam(required = false) String text,
            @RequestParam(name = "events", required = false) List<Long> eventIds,
            @RequestParam(name = "users", required = false) List<Long> userIds,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime rangeStart,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime rangeEnd,
            @PositiveOrZero @RequestParam(defaultValue = "0") int from,
            @Positive @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        log.info("Get comments by admin with \ntext: {}, \nevents: {}, \nusers: {}, \nrangeStart: {}, \nrangeEnd: {}, " +
                        "\nfrom: {}, \nsize: {}, \nIP of admin: {}", text, eventIds, userIds,
                rangeStart, rangeEnd, from, size, request.getRemoteAddr());

        return new ResponseEntity<>(commentService.getCommentsFiltered(text, eventIds, userIds,
                rangeStart, rangeEnd, from, size), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<CommentDto>> getCommentsByUser(@PathVariable Long userId) {
        log.info("Get comments by ADMIN with user id={}", userId);

        return new ResponseEntity<>(commentService.findByUserId(userId), HttpStatus.OK);
    }
}

