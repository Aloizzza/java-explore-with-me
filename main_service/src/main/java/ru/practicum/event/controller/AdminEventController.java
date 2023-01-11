package ru.practicum.event.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.AdminUpdateEventDto;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/events")
@AllArgsConstructor
public class AdminEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventDto> getAll(@RequestParam(required = false) List<Long> users,
                                 @RequestParam(required = false) List<String> states,
                                 @RequestParam(required = false) List<Long> categories,
                                 @RequestParam(required = false)
                                 @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                 @RequestParam(required = false)
                                 @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                 @RequestParam(defaultValue = "0") int from,
                                 @RequestParam(defaultValue = "10") int size) {
        log.info("get events by admin with param: userIds = {}, states = {}, categoryIds = {}, rangeStart = {}, " +
                "rangeEnd = {}, from = {}, size = {}", users, states, categories, rangeStart, rangeEnd, from, size);

        return eventService.getAllByAdmin(users, states, categories, rangeStart, rangeEnd,
                PageRequest.of(from / size, size));
    }

    @PatchMapping("/{eventId}/publish")
    public EventDto publish(@PathVariable Long eventId) {
        log.info("publish event with id {} by admin", eventId);

        return eventService.publish(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventDto reject(@PathVariable Long eventId) {
        log.info("reject event with id {} by admin", eventId);

        return eventService.reject(eventId);
    }

    @PutMapping("/{eventId}")
    public EventDto update(@PathVariable Long eventId,
                           @RequestBody AdminUpdateEventDto eventDto) {
        log.info("update event with id {} by admin", eventId);

        return eventService.updateByAdmin(eventId, eventDto);
    }
}
