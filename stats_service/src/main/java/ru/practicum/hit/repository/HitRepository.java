package ru.practicum.hit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.hit.model.Hit;
import ru.practicum.hit.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {
    @Query("SELECT new ru.practicum.hit.model.ViewStats(h.app, h.uri, count(distinct h.ip)) " +
            "FROM Hit h WHERE h.timestamp between ?1 AND ?2")
    List<ViewStats> findDistinct(LocalDateTime start, LocalDateTime end);

    Integer countByUri(String uri);

    List<Hit> findAllByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
