package ru.practicum.event.mapper;

import ru.practicum.event.dto.LocationDto;
import ru.practicum.event.model.Location;

public class LocationMapper {
    public static LocationDto toLocationDto(Location location) {
        return LocationDto
                .builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }

    public static Location toLocation(LocationDto locationDto) {
        return Location
                .builder()
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .build();
    }
}
