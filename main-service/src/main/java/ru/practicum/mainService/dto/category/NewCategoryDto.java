package ru.practicum.mainService.dto.category;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewCategoryDto {
    String name;
}
