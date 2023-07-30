package ru.practicum.mainService.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.practicum.mainService.models.Location;
import ru.practicum.mainService.utils.enums.StatusEnum;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EventFullDto extends EventShortDto {
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private StatusEnum state;
    private LocalDateTime createdOn;
    private String description;
    private Integer participantLimit = 0;
    private Location location;
}
