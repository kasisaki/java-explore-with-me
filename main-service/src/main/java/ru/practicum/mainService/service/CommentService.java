package ru.practicum.mainService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainService.dto.comment.CommentDto;
import ru.practicum.mainService.dto.comment.CommentShortDto;
import ru.practicum.mainService.dto.comment.NewCommentDto;
import ru.practicum.mainService.mappers.CommentMapper;
import ru.practicum.mainService.models.Comment;
import ru.practicum.mainService.models.Event;
import ru.practicum.mainService.models.User;
import ru.practicum.mainService.repositories.CommentRepository;
import ru.practicum.mainService.repositories.EventRepository;
import ru.practicum.mainService.repositories.UserRepository;
import ru.practicum.mainService.utils.exceptions.ConflictException;
import ru.practicum.mainService.utils.exceptions.ElementNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.mainService.mappers.CommentMapper.*;
import static ru.practicum.mainService.utils.DateUtils.handleDateRange;
import static ru.practicum.mainService.utils.enums.EventStatusEnum.PUBLISHED;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public List<CommentDto> findByUserId(Long userId) {
        checkUserExistsOrThrow(userId);
        return commentRepository.findAllByCommenterId(userId)
                .stream()
                .map(CommentMapper::commentToDto)
                .collect(Collectors.toList());
    }

    public List<CommentShortDto> findAllByEventId(Long eventId, Integer from, Integer size) {
        checkEventExistsOrThrow(eventId);
        return commentRepository.findAllByEventId(eventId, PageRequest.of(from, size))
                .stream()
                .map(CommentMapper::commentToShortDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentDto createComment(Long userId, Long eventId, NewCommentDto dto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ElementNotFoundException("User does not exist")
        );

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new ElementNotFoundException("Event does not exist")
        );

        if (event.getCommentsDisabled()) {
            throw new ConflictException("Event with id=" + eventId + "does not allow comments");
        }

        if (!event.getState().equals(PUBLISHED)) {
            throw new ConflictException("Cannot create comment for unpublished events");
        }

        return commentToDto(
                commentRepository.save(
                        newToComment(dto, user, event)
                )
        );
    }

    @Transactional
    public CommentDto updateComment(Long userId, Long eventId, Long commentId, NewCommentDto dto) {
        if (!commentRepository.existsByIdAndCommenterId(commentId, userId)) {
            throw new ConflictException("User with id=" + userId + " has not created the comment with id=" +
                    commentId + " and so cannot update it");
        }

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ElementNotFoundException("Comment does not exist")
        );

        checkUserExistsOrThrow(userId);
        checkEventExistsOrThrow(eventId);

        return commentToDto(
                commentRepository.save(
                        updateToComment(comment, dto)
                )
        );
    }

    public CommentDto findByCommentId(Long userId, Long eventId, Long commentId) {
        checkUserExistsOrThrow(userId);
        checkEventExistsOrThrow(eventId);
        return commentToDto(
                commentRepository.findById(commentId).orElseThrow(
                        () -> new ElementNotFoundException("Comment not found")
                )
        );
    }

    private void checkUserExistsOrThrow(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ElementNotFoundException("User " + userId + " does not exist");
        }
    }

    private void checkEventExistsOrThrow(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new ElementNotFoundException("Event " + eventId + " does not exist");
        }
    }

    public void deleteCommentByUser(Long commentId, Long userId) {
        if (userId != null) {
            checkUserExistsOrThrow(userId);
            if (!commentRepository.existsById(commentId)) {
                throw new ElementNotFoundException("Comment " + commentId + " does not exist");
            }
            if (!commentRepository.existsByIdAndCommenterId(commentId, userId)) {
                throw new ConflictException("User with id=" + userId + " has not created the comment with id=" +
                        commentId + " and so cannot delete it");
            }
        }
        commentRepository.deleteById(commentId);
    }

    public void deleteComment(Long commentId) {
        deleteCommentByUser(commentId, null);
    }

    public List<CommentDto> getCommentsFiltered(String text, List<Long> eventIds, List<Long> userIds,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                int from, int size) {
        handleDateRange(rangeStart, rangeEnd);

        Pageable pageable = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "created"));


        List<Comment> comments = commentRepository
                .getCommentsFilter(pageable, text, eventIds, userIds, rangeStart, rangeEnd)
                .toList();

        return comments.stream().map(CommentMapper::commentToDto).collect(Collectors.toList());
    }
}
