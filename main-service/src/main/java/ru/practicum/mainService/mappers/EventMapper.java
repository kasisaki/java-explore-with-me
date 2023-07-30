package ru.practicum.mainService.mappers;

import lombok.Data;
import ru.practicum.mainService.dto.event.EventFullDto;
import ru.practicum.mainService.dto.event.EventShortDto;
import ru.practicum.mainService.dto.event.NewEventDto;
import ru.practicum.mainService.dto.event.UpdateEventUserRequest;
import ru.practicum.mainService.models.Event;
import ru.practicum.mainService.models.Location;

import static ru.practicum.mainService.mappers.CategoryMapper.mapCategoryToCategoryResponseDto;
import static ru.practicum.mainService.mappers.UserMapper.userToShortDto;


@Data
public class EventMapper {

    public static EventFullDto eventToFullEventDto(Event event) {
        if (event == null) {
            return null;
        }
        EventFullDto eventDto = new EventFullDto();
        eventDto.setId(event.getId());
        eventDto.setTitle(event.getTitle());
        eventDto.setAnnotation(event.getAnnotation());
        eventDto.setCategory(mapCategoryToCategoryResponseDto(event.getCategory()));
        eventDto.setPaid(event.getPaid());
        eventDto.setEventDate(event.getEventDate());
        eventDto.setInitiator(userToShortDto(event.getInitiator()));
        eventDto.setDescription(event.getDescription());
        eventDto.setParticipantLimit(event.getParticipantLimit());
        eventDto.setState(event.getState());
        eventDto.setCreatedOn(event.getCreatedOn());
        eventDto.setLocation(event.getLocation());
        eventDto.setRequestModeration(event.getRequestModeration());
        return eventDto;
    }

    public static EventShortDto eventToShortDto(Event event) {
        EventShortDto shortEventResponseDto = new EventShortDto();
        shortEventResponseDto.setId(event.getId());
        shortEventResponseDto.setTitle(event.getTitle());
        shortEventResponseDto.setAnnotation(event.getAnnotation());
        shortEventResponseDto.setCategory(mapCategoryToCategoryResponseDto(event.getCategory()));
        shortEventResponseDto.setPaid(event.getPaid());
        shortEventResponseDto.setEventDate(event.getEventDate());
        shortEventResponseDto.setInitiator(userToShortDto(event.getInitiator()));
        return shortEventResponseDto;
    }

    // TODO:         event.setCategory(updateDto.getCategory());
    public static <T extends UpdateEventUserRequest> Event updateAdminDtoToEvent(T updateDto, Event event, Location location) {
        event.setAnnotation(updateDto.getAnnotation());
        event.setDescription(updateDto.getDescription());
        event.setEventDate(updateDto.getEventDate());
        event.setPaid(updateDto.getPaid());
        event.setLocation(location);
        event.setParticipantLimit(updateDto.getParticipantLimit());
        event.setTitle(updateDto.getTitle());
        event.setRequestModeration(updateDto.getRequestModeration());
        return event;
    }

    public static Event createDtoToEvent(NewEventDto newEvent, Location location) {
        if (newEvent == null) {
            return null;
        }
        Event event = new Event();
        event.setAnnotation(newEvent.getAnnotation());
        event.setCategory(newEvent.getCategory());
        event.setDescription(newEvent.getDescription());
        event.setEventDate(newEvent.getEventDate());
        event.setLocation(location);
        event.setRequestModeration(newEvent.getRequestModeration());
        event.setTitle(newEvent.getTitle());

        if (newEvent.getParticipantLimit() == null) {
            event.setParticipantLimit(0);
        } else {
            event.setParticipantLimit(newEvent.getParticipantLimit());
        }

        if (newEvent.getPaid() == null) {
            event.setPaid(false);
        } else {
            event.setPaid(newEvent.getPaid());
        }

        return event;
    }
}