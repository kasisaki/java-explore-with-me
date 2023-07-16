package ru.practicum.statsServer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.StatResponseDto;
import ru.practicum.statsServer.model.StatData;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<StatData, Long> {
    @Query(value =
            "SELECT new ru.practicum.dto.StatResponseDto(s.app, s.uri, COUNT(s.IP)) " +
                    "FROM StatData AS s " +
                    "WHERE s.created BETWEEN ?1 AND ?2 " +
                    "GROUP BY s.app, s.uri " +
                    "ORDER BY COUNT(s.IP) DESC")
    List<StatResponseDto> getStats(LocalDateTime start, LocalDateTime end);

    @Query(value =
            "SELECT new ru.practicum.dto.StatResponseDto(s.app, s.uri, COUNT(DISTINCT s.IP)) " +
                    "FROM StatData s " +
                    "WHERE s.created BETWEEN ?1 AND ?2 " +
                    "GROUP BY s.app, s.uri " +
                    "ORDER BY COUNT(DISTINCT s.IP) DESC")
    List<StatResponseDto> getStatsWithUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query(value =
            "SELECT new ru.practicum.dto.StatResponseDto(s.app, s.uri, COUNT(DIStiNCT s.IP)) " +
                    "FROM StatData s " +
                    "WHERE s.created BETWEEN ?1 AND ?2 " +
                    "AND s.uri IN ?3 " +
                    "GROUP BY s.app, s.uri " +
                    "ORDER BY COUNT(s.IP) DESC")
    List<StatResponseDto> getStatsWithUniqueIpByUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value =
            "SELECT new ru.practicum.dto.StatResponseDto(s.app, s.uri, COUNT(s.IP)) " +
                    "FROM StatData s " +
                    "WHERE s.created BETWEEN ?1 AND ?2 " +
                    "AND s.uri IN (?3) " +
                    "GROUP BY s.app, s.uri " +
                    "ORDER BY COUNT(s.IP) DESC")
    List<StatResponseDto> getStatsByUris(LocalDateTime start, LocalDateTime end, List<String> uris);
}
