package ru.practicum.event.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.dto.*;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.mapper.LocationMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Location;
import ru.practicum.event.model.State;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.LocationRepository;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.participation.repository.ParticipationRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.event.model.State.*;
import static ru.practicum.participation.model.StatusRequest.CONFIRMED;
import static ru.practicum.utility.Constant.DATE_TIME_FORMAT;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final ParticipationRepository participationRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

    @Override
    public List<EventDto> getAllByAdmin(List<Long> userIds, List<String> states, List<Long> categoryIds,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable) {
        List<State> stateList = new ArrayList<>();
        if (states != null) {
            stateList = states.stream()
                    .map(State::valueOf)
                    .collect(Collectors.toList());
        }

        return eventRepository.searchEventsByAdmin(userIds, stateList, categoryIds, pageable, rangeStart, rangeEnd)
                .stream()
                .map(EventMapper::toEventDto)
                .map(this::setConfirmedRequests)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShortEventDto> getByUserId(Long userId, Pageable pageable) {
        checkAndGetUser(userId);

        return eventRepository.findAllByInitiatorId(userId, pageable)
                .stream()
                .map(EventMapper::toShortEventDto)
                .map(this::setConfirmedRequests)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShortEventDto> getAll(String text, List<Long> categoryIds, Boolean paid, LocalDateTime rangeStart,
                                      LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, Pageable pageable) {
        List<ShortEventDto> events = eventRepository
                .searchEvents(text, categoryIds, paid, PUBLISHED, pageable, rangeStart, rangeEnd, onlyAvailable)
                .stream()
                .map(EventMapper::toShortEventDto)
                .map(this::setConfirmedRequests)
                .collect(Collectors.toList());

        if (sort != null) {
            switch (sort) {
                case "EVENT_DATE":
                    events = events
                            .stream()
                            .sorted(Comparator.comparing(ShortEventDto::getEventDate))
                            .collect(Collectors.toList());
                    break;
                case "VIEWS":
                    events = events
                            .stream()
                            .sorted(Comparator.comparing(ShortEventDto::getViews))
                            .collect(Collectors.toList());
                    break;
                default:
                    throw new BadRequestException("events can be sorted only by views or event date");
            }
        }

        return events
                .stream()
                .peek(shortEventDto -> incrementViews(shortEventDto.getId()))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventDto create(Long userId, NewEventDto eventDto) {
        User user = checkAndGetUser(userId);
        Event event = EventMapper.toEvent(eventDto);
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("event date is too late");
        }
        Location location = locationRepository.save(LocationMapper.toLocation(eventDto.getLocation()));
        event.setCategory(categoryRepository.findById(eventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("category not found")));
        event.setLocation(location);
        event.setInitiator(user);
        Event newEvent = eventRepository.save(event);

        return EventMapper.toEventDto(newEvent);
    }

    @Transactional
    @Override
    public EventDto publish(Long eventId) {
        Event event = checkAndGetEvent(eventId);
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new BadRequestException("event must have start date min after one hour of now");
        }
        if (!event.getState().equals(PENDING)) {
            throw new BadRequestException("state of event must be PENDING");
        }
        event.setState(PUBLISHED);
        EventDto eventDto = EventMapper.toEventDto(eventRepository.save(event));

        return setConfirmedRequests(eventDto);
    }

    @Override
    public EventDto getById(Long id) {
        Event event = checkAndGetEvent(id);
        if (!event.getState().equals(PUBLISHED)) {
            throw new BadRequestException("event is not published");
        }
        incrementViews(id);

        return setConfirmedRequests(EventMapper.toEventDto(event));
    }

    @Override
    public EventDto getByUser(Long eventId, Long userId) {
        Event event = checkAndGetEvent(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new BadRequestException("only initiator can get full information");
        }

        return setConfirmedRequests(EventMapper.toEventDto(event));
    }

    @Transactional
    @Override
    public EventDto reject(Long eventId) {
        Event event = checkAndGetEvent(eventId);
        event.setState(CANCELED);
        EventDto eventDto = EventMapper.toEventDto(eventRepository.save(event));

        return setConfirmedRequests(eventDto);
    }

    @Transactional
    @Override
    public EventDto update(Long userId, UserUpdateEventDto eventDto) {
        Event event = checkAndGetEvent(eventDto.getEventId());
        if (!event.getInitiator().getId().equals(userId)) {
            throw new BadRequestException("only initiator of event can update event");
        }
        if (event.getState().equals(PUBLISHED)) {
            throw new BadRequestException("published event can't be updated");
        }
        Optional.ofNullable(eventDto.getAnnotation()).ifPresent(event::setAnnotation);
        if (eventDto.getCategory() != null) {
            event.setCategory(categoryRepository.findById(eventDto.getCategory())
                    .orElseThrow(() -> new NotFoundException("category not found")));
        }
        Optional.ofNullable(eventDto.getDescription()).ifPresent(event::setDescription);
        if (eventDto.getEventDate() != null) {
            LocalDateTime date = LocalDateTime.parse(eventDto.getEventDate(), DATE_TIME_FORMAT);
            if (date.isBefore(LocalDateTime.now().plusHours(2))) {
                throw new BadRequestException("date event is too late");
            }
            event.setEventDate(date);
        }
        Optional.ofNullable(eventDto.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(eventDto.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(eventDto.getTitle()).ifPresent(event::setTitle);
        if (event.getState().equals(CANCELED)) {
            event.setState(PENDING);
        }
        EventDto returnEventDto = EventMapper.toEventDto(eventRepository.save(event));

        return setConfirmedRequests(returnEventDto);
    }

    @Transactional
    @Override
    public EventDto cancelByUser(Long eventId, Long userId) {
        Event event = checkAndGetEvent(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new BadRequestException("only initiator of event can cancel it");
        }
        if (!event.getState().equals(PENDING)) {
            throw new BadRequestException("only pending event can be canceled");
        }
        event.setState(CANCELED);
        EventDto eventDto = EventMapper.toEventDto(eventRepository.save(event));

        return setConfirmedRequests(eventDto);
    }

    @Transactional
    @Override
    public EventDto updateByAdmin(Long eventId, AdminUpdateEventDto eventDto) {
        Event event = checkAndGetEvent(eventId);
        Optional.ofNullable(eventDto.getAnnotation()).ifPresent(event::setAnnotation);
        if (eventDto.getCategory() != null) {
            event.setCategory(categoryRepository.findById(eventDto.getCategory())
                    .orElseThrow(() -> new NotFoundException("category not found")));
        }
        Optional.ofNullable(eventDto.getDescription()).ifPresent(event::setDescription);
        if (eventDto.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(eventDto.getEventDate(), DATE_TIME_FORMAT));
        }
        if (eventDto.getLocation() != null) {
            Location location = locationRepository.save(LocationMapper.toLocation(eventDto.getLocation()));
            event.setLocation(location);
        }
        Optional.ofNullable(eventDto.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(eventDto.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(eventDto.getRequestModeration()).ifPresent(event::setRequestModeration);
        Optional.ofNullable(eventDto.getTitle()).ifPresent(event::setTitle);
        EventDto returnEventDto = EventMapper.toEventDto(eventRepository.save(event));

        return setConfirmedRequests(returnEventDto);
    }

    private EventDto setConfirmedRequests(EventDto eventDto) {
        eventDto.setConfirmedRequests(participationRepository.countParticipationByEventIdAndStatus(eventDto.getId(),
                CONFIRMED));

        return eventDto;
    }

    private ShortEventDto setConfirmedRequests(ShortEventDto eventDto) {
        eventDto.setConfirmedRequests(participationRepository.countParticipationByEventIdAndStatus(eventDto.getId(),
                CONFIRMED));

        return eventDto;
    }

    private User checkAndGetUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("user with id = " + id + " not found"));
    }

    private void incrementViews(Long id) {
        Event event = checkAndGetEvent(id);
        long views = event.getViews() + 1;
        event.setViews(views);
    }

    private Event checkAndGetEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("event with id = " + id + " not found"));
    }
}
