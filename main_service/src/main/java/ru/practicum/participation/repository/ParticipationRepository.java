package ru.practicum.participation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.participation.model.Participation;
import ru.practicum.participation.model.StatusRequest;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    Participation findByEventIdAndRequester(Long eventId, User user);

    int countParticipationByEventIdAndStatus(Long eventId, StatusRequest status);

    List<Participation> findAllByRequesterId(Long userId);

    List<Participation> findAllByEventId(Long eventId);

    Optional<Participation> findByIdAndRequesterId(Long id, Long userId);
}
