package ru.practicum.participation.service;

import ru.practicum.participation.dto.ParticipationDto;

import java.util.List;

public interface ParticipationService {
    ParticipationDto create(Long userId, Long eventId);

    List<ParticipationDto> getAllByUserId(Long userId);

    List<ParticipationDto> getAllByUserIdForEvent(Long eventId, Long userId);

    ParticipationDto cancel(Long userId, Long reqId);

    ParticipationDto reject(Long eventId, Long userId, Long reqId);

    ParticipationDto confirm(Long eventId, Long userId, Long reqId);
}
