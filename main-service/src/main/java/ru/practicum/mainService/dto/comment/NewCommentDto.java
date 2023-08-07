package ru.practicum.mainService.dto.comment;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class NewCommentDto {
    @NotNull
    @Size(min = 1, max = 500, message = "wrong comment length")
    private String text;
}
