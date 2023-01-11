package ru.practicum.hit.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.hit.model.EndpointHit;
import ru.practicum.hit.model.ViewStats;
import ru.practicum.hit.service.HitService;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class HitController {
    private final HitService hitService;

    @PostMapping("/hit")
    public void saveHit(@RequestBody EndpointHit endpointHit) {
        log.info("save hit for uri {}", endpointHit.getUri());
        hitService.saveHit(endpointHit);
    }

    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam String start,
                                    @RequestParam String end,
                                    @RequestParam(required = false) List<String> uris,
                                    @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("get statistic for uris {}", uris);

        return hitService.getStats(start, end, uris, unique);
    }
}
