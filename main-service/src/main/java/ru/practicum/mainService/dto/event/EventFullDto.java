package ru.practicum.mainService.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.mainService.dto.category.CategoryDto;
import ru.practicum.mainService.dto.user.UserShortDto;
import ru.practicum.mainService.models.Location;
import ru.practicum.mainService.utils.enums.StatusEnum;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class EventFullDto {
    private Long id;
    private CategoryDto category;
    private UserShortDto initiator;
    private String publishedOn;
    private String annotation;
    private Boolean requestModeration;
    private StatusEnum state;
    private String title;
    private LocalDateTime createdOn;
    private String description;
    private LocalDateTime eventDate;
    private Long confirmedRequests;
    private Integer participantLimit = 0;
    private Location location;
    private Boolean paid;
    private Long views;
}
