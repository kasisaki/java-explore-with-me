package ru.practicum.mainService.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.mainService.dto.category.CategoryDto;
import ru.practicum.mainService.dto.location.LocationDto;
import ru.practicum.mainService.utils.enums.StateActionEnum;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.utils.Constants.DATE_PATTERN;

@Data
public class UpdateEventUserRequest {

    @Size(min = 20, max = 2000)
    private String annotation;

    private CategoryDto category;

    @Size(min = 20, max = 7000)
    private String description;

    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDateTime eventDate;

    private Boolean paid;

    private LocationDto location;

    private Integer participantLimit;

    private Boolean requestModeration = true; //default value

    @Size(min = 3, max = 120)
    private String title;

    private StateActionEnum stateAction;

}
