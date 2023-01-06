package ru.practicum.hit.service;

import ru.practicum.hit.model.EndpointHit;
import ru.practicum.hit.model.ViewStats;

import java.util.List;

public interface HitService {
    void saveHit(EndpointHit endpointHit);

    List<ViewStats> getStats(String start, String end, List<String> uris, Boolean uniq);
}
