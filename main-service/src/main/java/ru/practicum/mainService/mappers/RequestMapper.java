package ru.practicum.mainService.mappers;

import ru.practicum.mainService.dto.request.ParticipationRequestDto;
import ru.practicum.mainService.models.Event;
import ru.practicum.mainService.models.Request;
import ru.practicum.mainService.models.User;

public class RequestMapper {

    private RequestMapper() {
        throw new java.lang.UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Request dtoToRequest(ParticipationRequestDto dto, User user, Event event) {
        if (dto == null) return null;

        return Request.builder()
                .requester(user)
                .event(event)
                .created(dto.getCreated())
                .build();
    }

    public static ParticipationRequestDto requestToRequestDto(Request request) {
        if (request == null) return null;

        ParticipationRequestDto dto = new ParticipationRequestDto();
        dto.setRequester(request.getRequester().getId());
        dto.setEvent(request.getEvent().getId());
        dto.setStatus(request.getStatus());
        dto.setCreated(request.getCreated());
        dto.setId(request.getId());
        return dto;
    }
}
