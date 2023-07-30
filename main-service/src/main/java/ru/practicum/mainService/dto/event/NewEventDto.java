package ru.practicum.mainService.dto.event;

import lombok.Data;
import ru.practicum.mainService.dto.location.LocationDto;
import ru.practicum.mainService.models.Category;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class NewEventDto {

    @NotEmpty
    @Size(min = 20, max = 7000)
    private String annotation;

    @NotNull
    private Category category;

    @NotEmpty
    @Size(min = 20, max = 7000)
    private String description;

    @NotNull
    @Future
    private LocalDateTime eventDate;

    private Boolean paid;

    private LocationDto location;

    private Integer participantLimit;

    private Boolean requestModeration = true; //default value

    @NotEmpty
    @Size(min = 3, max = 120)
    private String title;

}
