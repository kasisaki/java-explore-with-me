package ru.practicum.mainService.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.utils.Constants.DATE_PATTERN;

@Data
public class CommentShortDto {
    @NotNull
    @Size(min = 1, max = 500, message = "wrong comment length")
    private String text;
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDateTime created;
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDateTime updated;
    private Long event;
    private Long commenter;
}
