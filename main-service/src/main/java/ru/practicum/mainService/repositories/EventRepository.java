package ru.practicum.mainService.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.mainService.models.Event;
import ru.practicum.mainService.utils.enums.StatusEnum;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Set<Event> findAllByIdIn(Set<Long> ids);

    @Query("SELECT e FROM Event e WHERE " +
            "(e.initiator.id IN (:userIds) OR :userIds IS NULL) AND " +
            "(e.state IN (:states) OR :states IS NULL) AND " +
            "(e.category.id IN (:categories) OR :categories IS NULL) AND " +
            "(e.eventDate BETWEEN :fromDate AND :toDate OR :fromDate IS NULL OR :toDate IS NULL)")
    Page<Event> getFullEventsFiltered(Pageable pageable,
                                      @Param("userIds") Set<Long> userIds,
                                      @Param("states") List<StatusEnum> states,
                                      @Param("categories") List<Long> categories,
                                      @Param("fromDate") LocalDateTime fromDate,
                                      @Param("toDate") LocalDateTime toDate);

}
