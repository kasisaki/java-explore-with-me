package ru.practicum.mainService.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.mainService.utils.enums.RequestStatusEnum;

import java.util.List;

@Data
@AllArgsConstructor
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private RequestStatusEnum status;
}
