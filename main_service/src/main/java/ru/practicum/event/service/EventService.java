package ru.practicum.event.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.event.dto.*;

import java.util.List;

public interface EventService {
    List<EventDto> getAllByAdmin(List<Long> userIds, List<String> states, List<Long> categoryIds,
                                 String rangeStart, String rangeEnd, PageRequest pageRequest);

    List<ShortEventDto> getByUserId(Long userId, PageRequest pageRequest);

    List<ShortEventDto> getAll(String text, List<Long> categoryIds, Boolean paid, String rangeStart, String rangeEnd,
                               Boolean onlyAvailable, String sort, PageRequest pageRequest);

    EventDto create(Long userId, NewEventDto eventDto);

    EventDto publish(Long eventId);

    EventDto getById(Long id);

    EventDto getByUser(Long eventId, Long userId);

    EventDto reject(Long eventId);

    EventDto update(Long userId, UserUpdateEventDto eventDto);

    EventDto cancelByUser(Long eventId, Long userId);

    EventDto updateByAdmin(Long eventId, AdminUpdateEventDto eventDto);
}
