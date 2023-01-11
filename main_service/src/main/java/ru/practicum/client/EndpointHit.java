package ru.practicum.client;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EndpointHit {
    private String app;

    private String uri;

    private String ip;

    private String timestamp;
}