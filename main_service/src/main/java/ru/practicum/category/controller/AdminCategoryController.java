package ru.practicum.category.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.service.CategoryService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/admin/categories")
@AllArgsConstructor
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PatchMapping
    public CategoryDto update(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("update category with id {}", categoryDto.getId());

        return categoryService.update(categoryDto);
    }

    @PostMapping
    public CategoryDto create(@Valid @RequestBody NewCategoryDto categoryDto) {
        log.info("create category {}", categoryDto.getName());

        return categoryService.create(categoryDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("delete category with id {}", id);
        categoryService.delete(id);
    }
}
