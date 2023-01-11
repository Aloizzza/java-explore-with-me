package ru.practicum.compilation.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/admin/compilations")
@AllArgsConstructor
public class AdminControllerCompilation {
    private final CompilationService compilationService;

    @PostMapping
    public CompilationDto create(@Valid @RequestBody NewCompilationDto compilationDto) {
        log.info("create new compilation {}", compilationDto.getTitle());

        return compilationService.create(compilationDto);
    }

    @PatchMapping("/{id}/events/{eventId}")
    public void addEventToCompilation(@PathVariable Long id,
                                      @PathVariable Long eventId) {
        log.info("add event with id {} to compilation with id {}", eventId, id);
        compilationService.addEventToCompilation(id, eventId);
    }

    @PatchMapping("/{id}/pin")
    public void addCompilationToMainPage(@PathVariable Long id) {
        log.info("add compilation with id {} to main page", id);
        compilationService.addCompilationToMainPage(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("delete compilation with id {}", id);
        compilationService.delete(id);
    }

    @DeleteMapping("/{id}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable Long id,
                                           @PathVariable Long eventId) {
        log.info("delete event with id {} from compilation with id {}", eventId, id);
        compilationService.deleteEventFromCompilation(id, eventId);
    }

    @DeleteMapping("/{id}/pin")
    public void deleteCompilationFromMainPage(@PathVariable Long id) {
        log.info("delete compilation with id {} from main page", id);
        compilationService.deleteCompilationFromMainPage(id);
    }
}
