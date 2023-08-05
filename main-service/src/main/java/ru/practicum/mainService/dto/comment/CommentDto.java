package ru.practicum.mainService.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

import static ru.practicum.utils.Constants.DATE_PATTERN;

@Data
public class CommentDto {
    private Long id;
    private String text;
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDateTime created;
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDateTime updated;
    private Long event;
    private Long commenter;
}
