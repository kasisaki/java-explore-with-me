package ru.practicum.mainService.dto.request;

import lombok.Data;
import ru.practicum.mainService.utils.enums.RequestStatusEnum;

import java.time.LocalDateTime;

@Data
public class ParticipationRequestDto {
    private LocalDateTime created;
    private Long event;
    private Long id;
    private Long requester;
    private RequestStatusEnum status;
}
