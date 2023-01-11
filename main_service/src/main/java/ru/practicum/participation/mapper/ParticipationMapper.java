package ru.practicum.participation.mapper;

import ru.practicum.participation.dto.ParticipationDto;
import ru.practicum.participation.model.Participation;

import static ru.practicum.utility.Constant.DATE_TIME_FORMAT;

public class ParticipationMapper {
    public static ParticipationDto toParticipationDto(Participation participation) {
        return ParticipationDto
                .builder()
                .id(participation.getId())
                .created(participation.getCreated().format(DATE_TIME_FORMAT))
                .event(participation.getEvent().getId())
                .requester(participation.getRequester().getId())
                .status(participation.getStatus())
                .build();
    }
}
