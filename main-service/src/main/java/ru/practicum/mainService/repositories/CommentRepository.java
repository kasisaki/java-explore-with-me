package ru.practicum.mainService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.mainService.models.Comment;
import ru.practicum.mainService.utils.enums.StatusEnum;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByCommenterId(Long userId);

    boolean existsByCommenterIdAndEventId(Long userId, Long eventId);

    Integer countByEventIdAndStatus(Long eventId, StatusEnum statusEnum);

    List<Comment> findAllByEventIdAndStatus(Long eventId, StatusEnum statusEnum);

    List<Comment> findAllByIdIn(List<Long> requestIds);

    List<Comment> findAllByEventId(Long eventId);
}
