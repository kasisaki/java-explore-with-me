package ru.practicum.mainService.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.mainService.utils.enums.EventStatusEnum;

@Data
@AllArgsConstructor
public class EventRequestStatusUpdateRequest {
    private Long requestId;
    private EventStatusEnum status;
}
