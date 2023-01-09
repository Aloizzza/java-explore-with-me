package ru.practicum.category.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAll(Pageable pageable);

    CategoryDto update(CategoryDto categoryDto);

    CategoryDto create(NewCategoryDto categoryDto);

    CategoryDto getById(Long id);

    void delete(Long id);
}
