package ru.practicum.mainService.mappers;

import ru.practicum.mainService.dto.category.CategoryDto;
import ru.practicum.mainService.models.Category;

public class CategoryMapper {
    public static Category mapCategoryDtoToCategory(CategoryDto categoryDto) {
        return Category.builder()
                .name(categoryDto.getName())
                .build();
    }

    public static CategoryDto mapCategoryToCategoryResponseDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
