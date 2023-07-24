package ru.practicum.mainService.dto.event;

import lombok.Data;
import ru.practicum.mainService.models.Category;
import ru.practicum.mainService.models.Location;
import ru.practicum.mainService.utils.enums.StateActionEnum;

import javax.validation.constraints.Future;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class UpdateEventAdminRequest {

    @Size(min = 20, max = 7000)
    private String annotation;

    private Category category;

    @Size(min = 20, max = 7000)
    private String description;

    @Future
    private LocalDateTime eventDate;

    private Boolean paid;

    private Location location;

    private Integer participantLimit;

    private Boolean requestModeration = true; //default value

    @Size(min = 3, max = 120)
    private String title;

    private StateActionEnum stateAction;

}
