package ru.practicum.mainService.mappers;

import ru.practicum.mainService.dto.category.CategoryDto;
import ru.practicum.mainService.dto.category.NewCategoryDto;
import ru.practicum.mainService.models.Category;


public class CategoryMapper {

    private CategoryMapper() {
        throw new java.lang.UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Category mapNewCategoryToCategory(NewCategoryDto categoryDto) {
        return Category.builder()
                .name(categoryDto.getName())
                .build();
    }

    public static Category mapCategoryDtoToCategory(CategoryDto categoryDto) {
        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .build();
    }

    public static CategoryDto categoryToDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static Category updateCategoryWithDto(NewCategoryDto categoryDto, Category category) {
        if (categoryDto.getName() != null) {
            category.setName(categoryDto.getName());
        }
        return category;
    }
}
