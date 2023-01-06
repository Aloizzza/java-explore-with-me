package ru.practicum.compilation.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.service.CompilationService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/compilations")
@AllArgsConstructor
public class PublicControllerCompilation {
    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getAll(@RequestParam(required = false) Boolean pinned,
                                       @RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size) {
        log.info("get compilations with param: pinned = {}, from = {}, size = {}", pinned, from, size);

        return compilationService.getAll(pinned, PageRequest.of(from / size, size));
    }

    @GetMapping("/{id}")
    public CompilationDto getById(@PathVariable Long id) {
        log.info("get compilation with id {}", id);

        return compilationService.getById(id);
    }
}
