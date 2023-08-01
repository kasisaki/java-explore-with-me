package ru.practicum.mainService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mainService.dto.request.ParticipationRequestDto;
import ru.practicum.mainService.mappers.RequestMapper;
import ru.practicum.mainService.models.Event;
import ru.practicum.mainService.models.Request;
import ru.practicum.mainService.models.User;
import ru.practicum.mainService.repositories.EventRepository;
import ru.practicum.mainService.repositories.RequestRepository;
import ru.practicum.mainService.repositories.UserRepository;
import ru.practicum.mainService.utils.exceptions.ConflictException;
import ru.practicum.mainService.utils.exceptions.ElementNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.mainService.mappers.RequestMapper.requestToRequestDto;
import static ru.practicum.mainService.utils.enums.EventStatusEnum.PUBLISHED;
import static ru.practicum.mainService.utils.enums.RequestStatusEnum.*;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public List<ParticipationRequestDto> findAllByUserId(Long userId) {
        checkUserExistsOrThrow(userId);
        return requestRepository.findAllByRequesterId(userId)
                .stream()
                .map(RequestMapper::requestToRequestDto)
                .collect(Collectors.toList());
    }

    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        checkUserExistsOrThrow(userId);
        User user = userRepository.findById(userId).orElseThrow();
        Event event = eventRepository.findById(eventId).orElseThrow();
        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ConflictException("Нельзя добавить повторный запрос");
        }

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Инициатор события не может добавить запрос на участие в своём событии");
        }

        if (!event.getState().equals(PUBLISHED)) {
            throw new ConflictException("Нельзя участвовать в неопубликованном событии");
        }

        if (event.getParticipantLimit() != null &&
                event.getParticipantLimit() != 0 &&
                requestRepository.countByEventIdAndStatus(eventId, ACCEPTED) >= event.getParticipantLimit()) {
            throw new ConflictException("У события достигнут лимит запросов на участие");
        }

        Request request = Request.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(user)
                .status(PENDING)
                .build();
        if (!event.getRequestModeration()) {
            request.setStatus(ACCEPTED);
        }

        return requestToRequestDto(requestRepository.save(request));
    }

    public ParticipationRequestDto updateRequest(Long userId, Long requestId) {
        checkUserExistsOrThrow(userId);
        Request request = requestRepository.findById(requestId).orElseThrow();
        request.setStatus(CANCELED);
        return requestToRequestDto(requestRepository.save(request));
    }

    private void checkUserExistsOrThrow(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ElementNotFoundException("User " + userId + " does not exist");
        }
    }
}
