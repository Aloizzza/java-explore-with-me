package ru.practicum.event.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.ShortEventDto;
import ru.practicum.event.dto.UserUpdateEventDto;
import ru.practicum.event.service.EventService;
import ru.practicum.participation.dto.ParticipationDto;
import ru.practicum.participation.service.ParticipationService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/events")
@AllArgsConstructor
public class UserEventController {
    private final EventService eventService;
    private final ParticipationService participationService;

    @GetMapping
    public List<ShortEventDto> getAll(@PathVariable Long userId,
                                      @RequestParam(defaultValue = "0") int from,
                                      @RequestParam(defaultValue = "10") int size) {
        log.info("get events added by user with id {}", userId);

        return eventService.getByUserId(userId, PageRequest.of(from / size, size));
    }

    @PostMapping
    public EventDto create(@PathVariable Long userId,
                           @Valid @RequestBody NewEventDto eventDto) {
        log.info("create event {} by user with id {}", eventDto.getTitle(), userId);

        return eventService.create(userId, eventDto);
    }

    @GetMapping("/{eventId}")
    public EventDto getById(@PathVariable Long userId,
                            @PathVariable Long eventId) {
        log.info("get event with id {} by user with id {}", eventId, userId);

        return eventService.getByUser(eventId, userId);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationDto> getParticipationRequest(@PathVariable Long userId,
                                                          @PathVariable Long eventId) {
        log.info("get participation requests by user with id {} of event with id {}", userId, eventId);

        return participationService.getAllByUserIdForEvent(eventId, userId);
    }

    @PatchMapping
    public EventDto update(@PathVariable Long userId,
                           @RequestBody UserUpdateEventDto eventDto) {
        log.info("update event by user with id {}", userId);

        return eventService.update(userId, eventDto);
    }

    @PatchMapping("/{eventId}")
    public EventDto cancelEvent(@PathVariable Long userId,
                                @PathVariable Long eventId) {
        log.info("cancel event with id {} by user with id {}", eventId, userId);

        return eventService.cancelByUser(eventId, userId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public ParticipationDto cancelParticipationRequest(@PathVariable Long userId,
                                                       @PathVariable Long eventId,
                                                       @PathVariable Long reqId) {
        log.info("reject participation requests {} by user with id {} of event with id {}", reqId, userId, eventId);

        return participationService.reject(eventId, userId, reqId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public ParticipationDto confirmParticipationRequest(@PathVariable Long userId,
                                                        @PathVariable Long eventId,
                                                        @PathVariable Long reqId) {
        log.info("confirm participation requests {} by user with id {} of event with id {}", reqId, userId, eventId);

        return participationService.confirm(eventId, userId, reqId);
    }
}
