package ru.practicum.mainService.mappers;

import lombok.Data;
import ru.practicum.mainService.dto.event.EventFullDto;
import ru.practicum.mainService.dto.event.EventShortDto;
import ru.practicum.mainService.dto.event.NewEventDto;
import ru.practicum.mainService.models.Category;
import ru.practicum.mainService.models.Event;
import ru.practicum.mainService.models.User;
import ru.practicum.mainService.utils.enums.StatusEnum;

import java.time.LocalDateTime;

import static ru.practicum.mainService.mappers.CategoryMapper.mapCategoryToCategoryResponseDto;
import static ru.practicum.mainService.mappers.UserMapper.userToShortDto;


@Data
public class EventMapper {
    public static Event toEvent(NewEventDto newEventDto, User user, Category category) {
        Event event = new Event();
        event.setAnnotation(newEventDto.getAnnotation());
        event.setCategory(category);
        event.setCreatedOn(LocalDateTime.now());
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(newEventDto.getEventDate());
        event.setInitiator(user);
        event.setLocation(newEventDto.getLocation());
        event.setPaid(newEventDto.getPaid());
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        event.setPublishedOn(null);
        event.setRequestModeration(newEventDto.getRequestModeration());
        event.setState(StatusEnum.PENDING);
        event.setTitle(newEventDto.getTitle());
        return event;
    }

    public static EventFullDto toEventDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(mapCategoryToCategoryResponseDto(event.getCategory()))
                .paid(event.getPaid())
                .eventDate(event.getEventDate())
                .initiator(userToShortDto(event.getInitiator()))
                .description(event.getDescription())
                .participantLimit(event.getParticipantLimit())
                .state(event.getState())
                .createdOn(event.getCreatedOn())
                .location(event.getLocation())
                .requestModeration(event.getRequestModeration())
                .build();
    }

    public static EventFullDto toEventFullDto(Event event, long views, long confirmedRequests) {
        EventFullDto eventFullDto = toEventDto(event);
        eventFullDto.setViews(views);
        eventFullDto.setConfirmedRequests(confirmedRequests);
        return eventFullDto;
    }

    public static EventShortDto toEventShortDto(Event event, Integer confirmedRequests, long views) {
        EventShortDto shortEventResponseDto = new EventShortDto();
        shortEventResponseDto.setId(event.getId());
        shortEventResponseDto.setTitle(event.getTitle());
        shortEventResponseDto.setAnnotation(event.getAnnotation());
        shortEventResponseDto.setCategory(mapCategoryToCategoryResponseDto(event.getCategory()));
        shortEventResponseDto.setPaid(event.getPaid());
        shortEventResponseDto.setConfirmedRequests(confirmedRequests);
        shortEventResponseDto.setEventDate(event.getEventDate());
        shortEventResponseDto.setInitiator(userToShortDto(event.getInitiator()));
        shortEventResponseDto.setViews(views);
        return shortEventResponseDto;
    }
}