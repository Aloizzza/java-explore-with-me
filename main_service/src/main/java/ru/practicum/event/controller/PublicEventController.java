package ru.practicum.event.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.EventClient;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.ShortEventDto;
import ru.practicum.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/events")
@AllArgsConstructor
public class PublicEventController {
    private final EventService eventService;
    private final EventClient eventClient;

    @GetMapping
    public List<ShortEventDto> getAll(@RequestParam(required = false) String text,
                                      @RequestParam(required = false) List<Long> categoryIds,
                                      @RequestParam(required = false) Boolean paid,
                                      @RequestParam(required = false) String rangeStart,
                                      @RequestParam(required = false) String rangeEnd,
                                      @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                      @RequestParam(required = false) String sort,
                                      @RequestParam(defaultValue = "0") int from,
                                      @RequestParam(defaultValue = "10") int size,
                                      HttpServletRequest httpServletRequest) {
        log.info("get events by param: text = {}, categoryIds = {}, paid = {}, rangeStart = {}, rangeEnd = {}, " +
                        "onlyAvailable = {}, sort = {}, from = {}, size = {}", text, categoryIds, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size);
        log.info("client ip: {}", httpServletRequest.getRemoteAddr());
        log.info("endpoint path: {}", httpServletRequest.getRequestURI());
        eventClient.createHit(httpServletRequest);

        return eventService.getAll(text, categoryIds, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                PageRequest.of(from / size, size));
    }

    @GetMapping("/{id}")
    public EventDto getById(@PathVariable Long id,
                            HttpServletRequest httpServletRequest) {
        log.info("get event with id {}", id);
        log.info("client ip: {}", httpServletRequest.getRemoteAddr());
        log.info("endpoint path: {}", httpServletRequest.getRequestURI());
        eventClient.createHit(httpServletRequest);

        return eventService.getById(id);
    }
}
