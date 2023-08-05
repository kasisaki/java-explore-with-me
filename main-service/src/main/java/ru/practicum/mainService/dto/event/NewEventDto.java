package ru.practicum.mainService.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.mainService.dto.category.CategoryDto;
import ru.practicum.mainService.dto.location.LocationDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.utils.Constants.DATE_PATTERN;

@Data
public class NewEventDto {
    @NotEmpty
    @Size(min = 20, max = 2000, message = "Annotation must be between 20 and 2000 characters")
    private String annotation;

    @NotNull
    private CategoryDto category;

    @NotEmpty
    @Size(min = 20, max = 7000, message = "Description must be between 20 and 7000 characters")
    private String description;

    @NotNull
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDateTime eventDate;

    @NotNull
    private LocationDto location;

    @NotEmpty
    @Size(min = 3, max = 120, message = "Title must be between 3 and 120 characters")
    private String title;

    private Boolean paid = false;

    private Boolean disableComments = false;

    @PositiveOrZero
    private Integer participantLimit;

    private Boolean requestModeration;
}