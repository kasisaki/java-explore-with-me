package ru.practicum.mainService.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.practicum.mainService.models.Location;
import ru.practicum.mainService.utils.enums.EventStatusEnum;

import java.time.LocalDateTime;

import static ru.practicum.utils.Constants.DATE_PATTERN;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EventFullDto extends EventShortDto {
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private EventStatusEnum state;
    @JsonFormat(pattern = DATE_PATTERN)
    private LocalDateTime createdOn;
    private String description;
    private Integer participantLimit = 0;
    private Location location;
}
