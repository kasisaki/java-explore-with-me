package ru.practicum.mainService.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainService.dto.category.CategoryDto;
import ru.practicum.mainService.dto.user.UserShortDto;

import java.time.LocalDateTime;

import static ru.practicum.utils.Constants.DATE_PATTERN;

@Data
@Builder
@NoArgsConstructor
public class EventShortDto {
    private Long id;
    private CategoryDto category;
    private UserShortDto initiator;
    private String annotation;
    private String title;
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDateTime eventDate;
    private Integer confirmedRequests;
    private Boolean paid;
    private Long views;

    public EventShortDto(Long id, CategoryDto category, UserShortDto initiator, String annotation, String title,
                         LocalDateTime eventDate, Integer confirmedRequests, Boolean paid, Long views) {
        this.id = id;
        this.category = category;
        this.initiator = initiator;
        this.annotation = annotation;
        this.title = title;
        this.eventDate = eventDate;
        this.confirmedRequests = confirmedRequests;
        this.paid = paid;
        this.views = views;
    }
}
