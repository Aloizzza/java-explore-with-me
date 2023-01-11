package ru.practicum.event.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.event.dto.*;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    List<EventDto> getAllByAdmin(List<Long> userIds, List<String> states, List<Long> categoryIds,
                                 LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    List<ShortEventDto> getByUserId(Long userId, Pageable pageable);

    List<ShortEventDto> getAll(String text, List<Long> categoryIds, Boolean paid, LocalDateTime rangeStart,
                               LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, Pageable pageable);

    EventDto create(Long userId, NewEventDto eventDto);

    EventDto publish(Long eventId);

    EventDto getById(Long id);

    EventDto getByUser(Long eventId, Long userId);

    EventDto reject(Long eventId);

    EventDto update(Long userId, UserUpdateEventDto eventDto);

    EventDto cancelByUser(Long eventId, Long userId);

    EventDto updateByAdmin(Long eventId, AdminUpdateEventDto eventDto);
}
