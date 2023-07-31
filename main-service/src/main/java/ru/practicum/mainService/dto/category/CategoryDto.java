package ru.practicum.mainService.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    long id;
    String name;

    public CategoryDto(int id) {
        this.id = id;
    }

}
