package ru.practicum.mainService.controller.authorised.general;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainService.dto.request.ParticipationRequestDto;
import ru.practicum.mainService.service.RequestService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
public class ParticipationRequestController {
    private final RequestService requestService;

    @GetMapping
    public ResponseEntity<List<ParticipationRequestDto>> findAllRequestsByUserId(@PathVariable Long userId) {
        log.info("Find all participation requests of User with id={}", userId);
        return new ResponseEntity<>(requestService.findAllByUserId(userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> createRequest(@PathVariable Long userId,
                                                                 @RequestParam Long eventId) {
        log.info("Create participation request from user with id={}, for event with id={}", userId, eventId);
        return new ResponseEntity<>(requestService.createRequest(userId, eventId), HttpStatus.CREATED);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> updateRequest(@PathVariable Long userId,
                                                                 @PathVariable Long requestId) {
        log.info("User with id={} requested cancelation of request with id={}", userId, requestId);
        return new ResponseEntity<>(requestService.updateRequest(userId, requestId), HttpStatus.OK);
    }
}
