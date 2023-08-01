package ru.practicum.mainService.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.mainService.models.Event;
import ru.practicum.mainService.utils.enums.EventStatusEnum;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Set<Event> findAllByIdIn(Set<Long> ids);

    @Query("SELECT e FROM Event e WHERE " +
            "(e.initiator.id IN :userIds OR :userIds IS NULL) AND " +
            "(e.state IN :states OR :states IS NULL) AND " +
            "(e.category.id IN :categories OR :categories IS NULL) AND " +
            "(e.eventDate BETWEEN :fromDate AND :toDate)")
    Page<Event> getFullEventsFiltered(@Param("userIds") Set<Long> userIds,
                                      @Param("states") List<EventStatusEnum> states,
                                      @Param("categories") List<Long> categories,
                                      @Param("fromDate") LocalDateTime fromDate,
                                      @Param("toDate") LocalDateTime toDate,
                                      Pageable pageable);

    Page<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    Optional<Event> findFirstByIdAndInitiatorId(Long eventId, Long userId);

    @Query("SELECT e FROM Event e WHERE " +
            "(lower(e.title) LIKE lower(concat('%', :text, '%')) OR :text IS NULL) AND " +
            "(e.category.id IN :categoriesId OR :categoriesId IS NULL) AND " +
            "(e.paid = :paid OR :paid IS NULL) AND " +
            "(e.eventDate BETWEEN :rangeStart AND :rangeEnd OR (:rangeStart IS NULL AND :rangeEnd IS NULL)) AND " +
            "((e.state = 'AVAILABLE' AND :onlyAvailable = TRUE) OR :onlyAvailable = FALSE)")
    List<Event> getShortEventsFilter(@Param("text") String text,
                                     @Param("categoriesId") List<Integer> categoriesId,
                                     @Param("paid") Boolean paid,
                                     @Param("rangeStart") LocalDateTime rangeStart,
                                     @Param("rangeEnd") LocalDateTime rangeEnd,
                                     @Param("onlyAvailable") Boolean onlyAvailable);
}
