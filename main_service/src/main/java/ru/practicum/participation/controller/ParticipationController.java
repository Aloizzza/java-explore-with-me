package ru.practicum.participation.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.participation.dto.ParticipationDto;
import ru.practicum.participation.service.ParticipationService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/requests")
@AllArgsConstructor
public class ParticipationController {
    private final ParticipationService participationService;

    @PostMapping
    public ParticipationDto create(@PathVariable Long userId,
                                   @RequestParam Long eventId) {
        log.info("create participation request by user {} to event {}", userId, eventId);

        return participationService.create(userId, eventId);
    }

    @GetMapping
    public List<ParticipationDto> getAllByUserId(@PathVariable Long userId) {
        log.info("get participation requests for user with id {}", userId);

        return participationService.getAllByUserId(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationDto cancel(@PathVariable Long userId,
                                   @PathVariable Long requestId) {
        log.info("cancel participation request {} by user {}", requestId, userId);

        return participationService.cancel(userId, requestId);
    }
}
