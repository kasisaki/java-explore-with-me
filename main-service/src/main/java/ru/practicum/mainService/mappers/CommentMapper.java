package ru.practicum.mainService.mappers;

import ru.practicum.mainService.dto.comment.CommentDto;
import ru.practicum.mainService.models.Comment;
import ru.practicum.mainService.models.Event;
import ru.practicum.mainService.models.User;

public class CommentMapper {

    private CommentMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Comment dtoToComment(CommentDto dto, User user, Event event) {
        if (dto == null) return null;

        return Comment.builder()
                .text(dto.getText())
                .commenter(user)
                .event(event)
                .created(dto.getCreated())
                .build();
    }

    public static CommentDto commentToDto(Comment comment) {
        if (comment == null) return null;

        CommentDto dto = new CommentDto();
        dto.setText(comment.getText());
        dto.setCommenter(comment.getCommenter().getId());
        dto.setEvent(comment.getEvent().getId());
        dto.setStatus(comment.getStatus());
        dto.setCreated(comment.getCreated());
        dto.setId(comment.getId());
        return dto;
    }
}
