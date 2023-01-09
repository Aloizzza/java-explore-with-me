package ru.practicum.event.mapper;

import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.ShortEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.user.mapper.UserMapper;

import java.time.LocalDateTime;

import static ru.practicum.event.model.State.PENDING;
import static ru.practicum.utility.Constant.DATE_TIME_FORMAT;

public class EventMapper {
    public static EventDto toEventDto(Event event) {
        return EventDto
                .builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .createdOn(event.getCreatedOn()
                        .format(DATE_TIME_FORMAT))
                .description(event.getDescription())
                .eventDate(event.getEventDate()
                        .format(DATE_TIME_FORMAT))
                .initiator(UserMapper.toShortUserDto(event.getInitiator()))
                .location(LocationMapper.toLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static ShortEventDto toShortEventDto(Event event) {
        return ShortEventDto
                .builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .eventDate(event.getEventDate()
                        .format(DATE_TIME_FORMAT))
                .initiator(UserMapper.toShortUserDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static Event toEvent(NewEventDto eventDto) {
        return Event
                .builder()
                .annotation(eventDto.getAnnotation())
                .description(eventDto.getDescription())
                .eventDate(LocalDateTime
                        .parse(eventDto.getEventDate(), DATE_TIME_FORMAT))
                .paid(eventDto.getPaid())
                .participantLimit(eventDto.getParticipantLimit())
                .requestModeration(eventDto.getRequestModeration())
                .title(eventDto.getTitle())
                .state(PENDING)
                .createdOn(LocalDateTime.now())
                .build();
    }
}
