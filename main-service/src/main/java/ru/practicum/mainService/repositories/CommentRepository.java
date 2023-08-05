package ru.practicum.mainService.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.mainService.models.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByCommenterId(Long commenterId);

    boolean existsByCommenterIdAndEventId(Long commenterId, Long eventId);

    List<Comment> findAllByIdIn(List<Long> requestIds);

    List<Comment> findAllByEventId(Long eventId);

    @Query("SELECT e FROM Comment e WHERE " +
            "lower(e.text) LIKE lower(concat('%', :searchText, '%')) OR :searchText IS NULL AND " +
            "e.event.id IN :eventIds OR :eventIds IS NULL AND " +
            "e.commenter.id IN :userIds OR :userIds IS NULL AND " +
            "(e.created >= :rangeStart) AND (e.created <= :rangeEnd)")
    Page<Comment> getCommentsFilter(Pageable pageable,
                                    @Param("searchText") String text,
                                    @Param("eventIds") List<Long> eventIds,
                                    @Param("userIds") List<Long> userIds,
                                    @Param("rangeStart") LocalDateTime rangeStart,
                                    @Param("rangeEnd") LocalDateTime rangeEnd);
}
