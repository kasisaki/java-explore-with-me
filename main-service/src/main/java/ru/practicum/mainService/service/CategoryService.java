package ru.practicum.mainService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.mainService.dto.category.CategoryDto;
import ru.practicum.mainService.dto.category.NewCategoryDto;
import ru.practicum.mainService.mappers.CategoryMapper;
import ru.practicum.mainService.repositories.CategoryRepository;
import ru.practicum.mainService.utils.exceptions.ElementNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.mainService.mappers.CategoryMapper.*;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        return categoryToDto(
                categoryRepository.save(
                        mapNewCategoryToCategory(newCategoryDto)
                )
        );
    }

    public void deleteCategory(Long catId) {
        if (!categoryRepository.existsById(catId)) {
            throw new ElementNotFoundException("Category with id " + catId + " does not exist");
        }
        categoryRepository.deleteById(catId);
    }

    public CategoryDto updateCategory(Long catId, NewCategoryDto newCategoryDto) {
        return categoryToDto(
                categoryRepository.save(
                        updateCategoryWithDto(
                                newCategoryDto,
                                categoryRepository.findById(catId).orElseThrow()
                        )
                )
        );
    }

    public List<CategoryDto> getCategories(Integer from, Integer size) {
        return categoryRepository.findAll(PageRequest.of(from, size))
                .stream()
                .map(CategoryMapper::categoryToDto)
                .collect(Collectors.toList());
    }

    public CategoryDto getCategory(Long catId) {
        return categoryToDto(categoryRepository.findById(catId).orElseThrow());
    }
}
