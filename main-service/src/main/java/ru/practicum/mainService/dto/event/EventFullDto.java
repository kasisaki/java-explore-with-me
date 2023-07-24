package ru.practicum.mainService.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.mainService.dto.category.CategoryDto;
import ru.practicum.mainService.dto.user.UserShortDto;
import ru.practicum.mainService.models.Location;

@Data
@Builder
@AllArgsConstructor
public class EventFullDto {
    private Long id;
    private CategoryDto category;
    private UserShortDto initiator;
    private String publishedOn;
    private String requestModeration;
    private String state;
    private String title;
    private String createdOn;
    private String description;
    private String eventDate;
    private Integer confirmedRequests;
    private Integer participantLimit = 0;
    private Location location;
    private Boolean paid;
    private Long views;
}
