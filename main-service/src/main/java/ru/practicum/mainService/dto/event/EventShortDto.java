package ru.practicum.mainService.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainService.dto.category.CategoryDto;
import ru.practicum.mainService.dto.user.UserShortDto;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {
    private Long id;
    private CategoryDto category;
    private UserShortDto initiator;
    private String annotation;
    private String title;
    private LocalDateTime eventDate;
    private Integer confirmedRequests;
    private Boolean paid;
    private Long views;
}
