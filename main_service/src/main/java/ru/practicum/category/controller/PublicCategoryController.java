package ru.practicum.category.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categories")
@AllArgsConstructor
public class PublicCategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getAll(@RequestParam(defaultValue = "0") int from,
                                    @RequestParam(defaultValue = "10") int size) {
        log.info("get categories with param: from = {}, size = {}", from, size);

        return categoryService.getAll(PageRequest.of(from / size, size));
    }

    @GetMapping("/{id}")
    public CategoryDto getById(@PathVariable Long id) {
        log.info("get category with id {}", id);

        return categoryService.getById(id);
    }
}
