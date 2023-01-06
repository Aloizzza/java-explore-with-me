package ru.practicum.compilation.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getAll(Boolean pinned, PageRequest pageRequest);

    CompilationDto create(NewCompilationDto compilationDto);

    CompilationDto getById(Long id);

    void addEventToCompilation(Long id, Long eventId);

    void addCompilationToMainPage(Long id);

    void delete(Long id);

    void deleteEventFromCompilation(Long id, Long eventId);

    void deleteCompilationFromMainPage(Long id);
}