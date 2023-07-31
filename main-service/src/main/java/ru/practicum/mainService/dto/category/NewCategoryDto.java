package ru.practicum.mainService.dto.category;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class NewCategoryDto {
    @NotEmpty
    @Size(max = 50)
    private String name;
}
