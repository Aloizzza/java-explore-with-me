package ru.practicum.participation.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.participation.dto.ParticipationDto;
import ru.practicum.participation.mapper.ParticipationMapper;
import ru.practicum.participation.model.Participation;
import ru.practicum.participation.repository.ParticipationRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.event.model.State.PUBLISHED;
import static ru.practicum.participation.model.StatusRequest.*;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class ParticipationServiceImpl implements ParticipationService {
    private final ParticipationRepository participationRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public ParticipationDto create(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user with id = " + userId + " not found"));
        Event event = checkAndGetEvent(eventId);
        if (participationRepository.findByEventIdAndRequester(eventId, user) != null) {
            throw new BadRequestException("participation request already exists");
        }
        Participation participation = Participation
                .builder()
                .created(LocalDateTime.now())
                .requester(user)
                .event(event)
                .status(CONFIRMED)
                .build();
        if (userId.equals(participation.getEvent().getInitiator().getId())) {
            throw new BadRequestException("requester can't be initiator of event");
        }
        if (!participation.getEvent().getState().equals(PUBLISHED)) {
            throw new BadRequestException("event is not published");
        }
        if (participation.getEvent().getParticipantLimit() <= participationRepository
                .countParticipationByEventIdAndStatus(eventId, CONFIRMED)) {
            throw new BadRequestException("limit of requests for participation has been exhausted");
        }
        if (Boolean.TRUE.equals(participation.getEvent().getRequestModeration())) {
            participation.setStatus(PENDING);
        }
        Participation newParticipation = participationRepository.save(participation);

        return ParticipationMapper.toParticipationDto(newParticipation);
    }

    @Override
    public List<ParticipationDto> getAllByUserId(Long userId) {
        return participationRepository.findAllByRequesterId(userId)
                .stream()
                .map(ParticipationMapper::toParticipationDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParticipationDto> getAllByUserIdForEvent(Long eventId, Long userId) {
        Event event = checkAndGetEvent(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new BadRequestException("only initiator of event can reject participation request to this event");
        }

        return participationRepository.findAllByEventId(eventId)
                .stream()
                .map(ParticipationMapper::toParticipationDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ParticipationDto cancel(Long userId, Long reqId) {
        Participation participation = participationRepository.findByIdAndRequesterId(reqId, userId)
                .orElseThrow(() -> new BadRequestException("only requester can cancel participation request"));
        participation.setStatus(CANCELED);
        Participation savedParticipation = participationRepository.save(participation);

        return ParticipationMapper.toParticipationDto(savedParticipation);
    }

    @Transactional
    @Override
    public ParticipationDto reject(Long eventId, Long userId, Long reqId) {
        Participation participation = checkAndGetParticipation(reqId);
        Event event = checkAndGetEvent(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new BadRequestException("only initiator of event can reject request to this event");
        }
        if (!eventId.equals(participation.getEvent().getId())) {
            throw new BadRequestException("eventId not equals eventId of participation request");
        }
        participation.setStatus(REJECTED);
        Participation savedParticipation = participationRepository.save(participation);

        return ParticipationMapper.toParticipationDto(savedParticipation);
    }

    @Transactional
    @Override
    public ParticipationDto confirm(Long eventId, Long userId, Long reqId) {
        Participation participation = checkAndGetParticipation(reqId);
        Event event = checkAndGetEvent(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new BadRequestException("only initiator of event can reject participation request to this event");
        }
        if (!eventId.equals(participation.getEvent().getId())) {
            throw new BadRequestException("eventId not equals eventId of participation request");
        }
        if (!participation.getStatus().equals(PENDING)) {
            throw new BadRequestException("only participation request with status pending can be confirmed");
        }
        if (event.getParticipantLimit() <= participationRepository
                .countParticipationByEventIdAndStatus(eventId, CONFIRMED)) {
            participation.setStatus(REJECTED);
        } else {
            participation.setStatus(CONFIRMED);
        }
        Participation savedParticipation = participationRepository.save(participation);

        return ParticipationMapper.toParticipationDto(savedParticipation);
    }

    private Event checkAndGetEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("event with id = " + id + " not found"));
    }

    private Participation checkAndGetParticipation(Long reqId) {
        return participationRepository.findById(reqId)
                .orElseThrow(() -> new NotFoundException("participation request with id = " + reqId + " not found"));
    }
}
