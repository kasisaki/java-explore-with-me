package ru.practicum.mainService.mappers;

import ru.practicum.mainService.dto.request.ParticipationRequestDto;
import ru.practicum.mainService.models.Request;

public class RequestMapper {
    public static Request dtoToRequest(ParticipationRequestDto dto) {
        if (dto == null) return null;

        return Request.builder()
                .userId(dto.getRequester())
                .eventId(dto.getEvent())
                .created(dto.getCreated())
                .build();
    }

    public static ParticipationRequestDto requestToRequestDto(Request request) {
        if (request == null) return null;

        ParticipationRequestDto dto = new ParticipationRequestDto();
        dto.setRequester(request.getUserId());
        dto.setEvent(request.getEventId());
        dto.setStatus(request.getStatus());
        dto.setCreated(request.getCreated());
        dto.setId(request.getId());
        return dto;
    }
}
