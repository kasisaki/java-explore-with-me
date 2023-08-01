package ru.practicum.mainService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.mainService.dto.request.ParticipationRequestDto;
import ru.practicum.mainService.models.Request;
import ru.practicum.mainService.utils.enums.RequestStatusEnum;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<ParticipationRequestDto> findAllByUserId(Long userId);

    boolean existsByUserIdAndEventId(Long userId, Long eventId);

    Integer countByEventIdAndStatus(Long eventId, RequestStatusEnum requestStatusEnum);
}
