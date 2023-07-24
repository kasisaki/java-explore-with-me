package ru.practicum.mainService.dto.request;

import lombok.Data;
import ru.practicum.mainService.utils.enums.StatusEnum;

import java.time.LocalDateTime;

@Data
public class ParticipationRequestDto {
    private LocalDateTime created;
    private Integer event;
    private Integer id;
    private Integer requester;
    private StatusEnum status;
}
