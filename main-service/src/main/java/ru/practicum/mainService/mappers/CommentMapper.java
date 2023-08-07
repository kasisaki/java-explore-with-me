package ru.practicum.mainService.mappers;

import ru.practicum.mainService.dto.comment.CommentDto;
import ru.practicum.mainService.dto.comment.CommentShortDto;
import ru.practicum.mainService.dto.comment.NewCommentDto;
import ru.practicum.mainService.models.Comment;
import ru.practicum.mainService.models.Event;
import ru.practicum.mainService.models.User;

import java.time.LocalDateTime;

public class CommentMapper {

    private CommentMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Comment newToComment(NewCommentDto dto, User user, Event event) {
        if (dto == null) return null;

        return Comment.builder()
                .text(dto.getText())
                .commenter(user)
                .event(event)
                .created(LocalDateTime.now())
                .build();
    }

    public static Comment updateToComment(Comment comment, NewCommentDto dto) {
        if (dto == null) return null;

        comment.setUpdated(LocalDateTime.now());
        comment.setText(dto.getText());
        return comment;
    }

    public static CommentDto commentToDto(Comment comment) {
        if (comment == null) return null;

        CommentDto dto = new CommentDto();
        dto.setText(comment.getText());
        dto.setCommenter(comment.getCommenter().getId());
        dto.setEvent(comment.getEvent().getId());
        dto.setCreated(comment.getCreated());
        dto.setUpdated(comment.getUpdated());
        dto.setId(comment.getId());
        return dto;
    }

    public static CommentShortDto commentToShortDto(Comment comment) {
        if (comment == null) return null;

        CommentShortDto dto = new CommentShortDto();
        dto.setText(comment.getText());
        dto.setCommenter(comment.getCommenter().getId());
        dto.setEvent(comment.getEvent().getId());
        dto.setCreated(comment.getCreated());
        return dto;
    }
}
