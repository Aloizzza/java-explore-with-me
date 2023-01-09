package ru.practicum.hit.mapper;

import ru.practicum.hit.model.EndpointHit;
import ru.practicum.hit.model.Hit;
import ru.practicum.hit.model.ViewStats;

import java.time.LocalDateTime;

import static ru.practicum.utility.ConstantStats.DATE_TIME_FORMAT;

public class HitMapper {
    public static Hit toHit(EndpointHit endpointHit) {
        return Hit
                .builder()
                .id(endpointHit.getId())
                .app(endpointHit.getApp())
                .ip(endpointHit.getIp())
                .uri(endpointHit.getUri())
                .timestamp(LocalDateTime.parse(endpointHit.getTimestamp(), DATE_TIME_FORMAT))
                .build();
    }

    public static ViewStats toViewStats(Hit hit) {
        return ViewStats
                .builder()
                .app(hit.getApp())
                .uri(hit.getUri())
                .build();
    }
}