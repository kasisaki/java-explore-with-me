package ru.practicum.mainService.dto.comment;

import lombok.Data;
import ru.practicum.mainService.utils.enums.StatusEnum;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;
    private String text;
    private LocalDateTime created;
    private Long event;
    private Long commenter;
    private StatusEnum status;
}
