package ru.practicum.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    @Query("SELECT e FROM Event AS e " +
            "WHERE ((:users) IS NULL OR e.initiator.id IN :users) " +
            "AND ((:states) IS NULL OR e.state IN :states) " +
            "AND ((:categories) IS NULL OR e.category.id IN :categories) " +
            "AND ((CAST(:start AS date) is null or CAST(:end AS date) is null) " +
            "OR (e.eventDate between :start AND :end))")
    Page<Event> searchEventsByAdmin(List<Long> users, List<State> states, List<Long> categories, Pageable pageable,
                                    LocalDateTime start, LocalDateTime end);

    @Query("SELECT e FROM Event AS e " +
            "WHERE (lower(e.annotation) like lower(concat('%', :text, '%')) " +
            "OR lower(e.description) like lower(concat('%', :text, '%'))) " +
            "AND ((:categoryIds) IS NULL OR e.category.id IN :categoryIds) " +
            "AND e.paid = :paid " +
            "AND ((:onlyAvailable) IS NULL OR ((:onlyAvailable = true) " +
            "AND (e.confirmedRequests>=e.participantLimit or e.participantLimit = 0)) OR ((:onlyAvailable = false))) " +
            "AND e.state IN :state " +
            "AND ((CAST(:start AS date) is null or CAST(:end AS date) is null) " +
            "OR (e.eventDate between :start AND :end))")
    Page<Event> searchEvents(String text, List<Long> categoryIds, Boolean paid, State state, Pageable pageable,
                             LocalDateTime start, LocalDateTime end, Boolean onlyAvailable);

    List<Event> findAllByCategoryId(Long categoryId);
}
