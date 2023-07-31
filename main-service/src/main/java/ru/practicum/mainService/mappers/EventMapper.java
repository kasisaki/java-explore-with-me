package ru.practicum.mainService.mappers;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.mainService.dto.event.EventFullDto;
import ru.practicum.mainService.dto.event.EventShortDto;
import ru.practicum.mainService.dto.event.NewEventDto;
import ru.practicum.mainService.dto.event.UpdateEventUserRequest;
import ru.practicum.mainService.dto.user.UserShortDto;
import ru.practicum.mainService.models.Event;
import ru.practicum.mainService.models.Location;

import static ru.practicum.mainService.mappers.CategoryMapper.categoryToDto;
import static ru.practicum.mainService.mappers.CategoryMapper.mapCategoryDtoToCategory;
import static ru.practicum.mainService.mappers.UserMapper.userShortDtoToUser;
import static ru.practicum.mainService.mappers.UserMapper.userToShortDto;


@Data
@Slf4j
public class EventMapper {

    public static EventFullDto eventToFullEventDto(Event event) {
        if (event == null) {
            return null;
        }
        EventFullDto eventDto = new EventFullDto();
        eventDto.setId(event.getId());
        eventDto.setTitle(event.getTitle());
        eventDto.setAnnotation(event.getAnnotation());
        eventDto.setCategory(categoryToDto(event.getCategory()));
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
        shortEventResponseDto.setCategory(categoryToDto(event.getCategory()));
        shortEventResponseDto.setPaid(event.getPaid());
        shortEventResponseDto.setEventDate(event.getEventDate());
        shortEventResponseDto.setInitiator(userToShortDto(event.getInitiator()));
        return shortEventResponseDto;
    }

    // TODO:         event.setCategory(updateDto.getCategory());
    public static <T extends UpdateEventUserRequest> Event updateAdminDtoToEvent(T updateDto, Event event, Location location) {
        if (updateDto.getAnnotation() != null) {
            event.setAnnotation(updateDto.getAnnotation());
        }
        if (updateDto.getDescription() != null) {
            event.setDescription(updateDto.getDescription());
        }
        if (updateDto.getEventDate() != null) {
            event.setEventDate(updateDto.getEventDate());
        }
        if (updateDto.getPaid() != null) {
            event.setPaid(updateDto.getPaid());
        }
        if (updateDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateDto.getParticipantLimit());
        }
        if (updateDto.getTitle() != null) {
            event.setTitle(updateDto.getTitle());
        }
        if (updateDto.getAnnotation() != null) {
            event.setRequestModeration(updateDto.getRequestModeration());
        }

        event.setLocation(location);
        return event;
    }

    public static Event createDtoToEvent(NewEventDto newEvent, Location location, UserShortDto user) {
        if (newEvent == null) {
            return null;
        }
        Event event = new Event();
        event.setAnnotation(newEvent.getAnnotation());
        event.setCategory(mapCategoryDtoToCategory(newEvent.getCategory()));
        event.setDescription(newEvent.getDescription());
        event.setEventDate(newEvent.getEventDate());
        event.setLocation(location);
        event.setRequestModeration(newEvent.getRequestModeration());
        event.setTitle(newEvent.getTitle());

        event.setInitiator(userShortDtoToUser(user));

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