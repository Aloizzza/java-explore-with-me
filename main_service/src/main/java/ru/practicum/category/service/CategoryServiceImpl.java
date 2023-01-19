package ru.practicum.category.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto) {
        Category category = getAndCheckCategory(categoryDto.getId());
        if (categoryRepository.findByName(categoryDto.getName()).isPresent()) {
            throw new ConflictException("category is already exists");
        }
        category.setName(categoryDto.getName());

        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto create(NewCategoryDto categoryDto) {
        String name = categoryDto.getName();
        if (categoryRepository.findByName(name).isPresent()) {
            throw new ConflictException("category is already exists");
        }
        Category newCategory = categoryRepository.save(CategoryMapper.toCategory(categoryDto));

        return CategoryMapper.toCategoryDto(newCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getById(Long id) {
        return CategoryMapper.toCategoryDto(getAndCheckCategory(id));
    }

    @Override
    public void delete(Long id) {
        if (!eventRepository.findAllByCategoryId(id).isEmpty()) {
            throw new BadRequestException("only category without event can be deleted");
        }
        categoryRepository.delete(getAndCheckCategory(id));
    }

    private Category getAndCheckCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id = " + id + " is not found"));
    }
}
