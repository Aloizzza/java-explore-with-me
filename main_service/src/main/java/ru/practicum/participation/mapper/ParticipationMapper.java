package ru.practicum.participation.mapper;

import ru.practicum.participation.dto.ParticipationDto;
import ru.practicum.participation.model.Participation;

import java.time.format.DateTimeFormatter;

public class ParticipationMapper {
    public static ParticipationDto toParticipationDto(Participation participation) {
        return ParticipationDto
                .builder()
                .id(participation.getId())
                .created(participation.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .event(participation.getEvent().getId())
                .requester(participation.getRequester().getId())
                .status(participation.getStatus())
                .build();
    }
}
