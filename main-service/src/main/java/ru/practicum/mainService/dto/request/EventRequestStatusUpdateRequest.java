package ru.practicum.mainService.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.mainService.utils.enums.StatusEnum;

@Data
@AllArgsConstructor
public class EventRequestStatusUpdateRequest {
    private Long requestId;
    private StatusEnum status;
}
