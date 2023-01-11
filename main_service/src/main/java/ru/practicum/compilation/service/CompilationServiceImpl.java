package ru.practicum.compilation.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.dto.ShortEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.participation.repository.ParticipationRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.participation.model.StatusRequest.CONFIRMED;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final ParticipationRepository participationRepository;

    @Override
    public List<CompilationDto> getAll(Boolean pinned, Pageable pageable) {
        if (pinned == null) {

            return compilationRepository.findAll(pageable)
                    .stream()
                    .map(CompilationMapper::toCompilationDto)
                    .collect(Collectors.toList());
        }

        return compilationRepository.findAllByPinned(pinned, pageable)
                .stream()
                .map(CompilationMapper::toCompilationDto)
                .map(this::setViewsAndConfirmedRequests)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CompilationDto create(NewCompilationDto compilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(compilationDto);
        List<Event> events = eventRepository.findAllById(compilationDto.getEvents());
        compilation.setEvents(events);
        Compilation newCompilation = compilationRepository.save(compilation);

        return setViewsAndConfirmedRequests(CompilationMapper.toCompilationDto(newCompilation));
    }

    @Override
    public CompilationDto getById(Long id) {
        return setViewsAndConfirmedRequests(CompilationMapper.toCompilationDto(getAndCheckCompilation(id)));
    }

    @Transactional
    @Override
    public void addEventToCompilation(Long id, Long eventId) {
        Compilation compilation = getAndCheckCompilation(id);
        compilation.getEvents().add(getAndCheckEvent(eventId));
        compilationRepository.save(compilation);
    }

    @Transactional
    @Override
    public void addCompilationToMainPage(Long id) {
        Compilation compilation = getAndCheckCompilation(id);
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        compilationRepository.delete(getAndCheckCompilation(id));
    }

    @Transactional
    @Override
    public void deleteEventFromCompilation(Long id, Long eventId) {
        Compilation compilation = getAndCheckCompilation(id);
        compilation.getEvents().remove(getAndCheckEvent(eventId));
        compilationRepository.save(compilation);
    }

    @Transactional
    @Override
    public void deleteCompilationFromMainPage(Long id) {
        Compilation compilation = getAndCheckCompilation(id);
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    private CompilationDto setViewsAndConfirmedRequests(CompilationDto compilationDto) {
        compilationDto.setEvents(compilationDto.getEvents()
                .stream()
                .map(this::setConfirmedRequests)
                .collect(Collectors.toList()));

        return compilationDto;
    }

    private ShortEventDto setConfirmedRequests(ShortEventDto eventDto) {
        eventDto.setConfirmedRequests(
                participationRepository.countParticipationByEventIdAndStatus(eventDto.getId(), CONFIRMED));

        return eventDto;
    }

    private Compilation getAndCheckCompilation(Long id) {
        return compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("compilation with id = " + id + " not found"));
    }

    private Event getAndCheckEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("event with id = " + id + " not found"));
    }
}
