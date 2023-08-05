package ru.practicum.mainService.dto.comment;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class CommentShortDto {
    @NotNull
    @Size(min = 1, max = 500, message = "wrong comment length")
    private String text;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Long event;
    private Long commenter;
}
