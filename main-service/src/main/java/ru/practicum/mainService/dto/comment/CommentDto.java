package ru.practicum.mainService.dto.comment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;
    private String text;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Long event;
    private Long commenter;
}
