package ru.practicum.mainService.controller.authorised.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainService.dto.category.CategoryDto;
import ru.practicum.mainService.dto.category.NewCategoryDto;
import ru.practicum.mainService.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {

        log.info("Get events with \ncategory name: {}", newCategoryDto.getName());

        return new ResponseEntity<>(categoryService.createCategory(newCategoryDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable @Positive Long catId) {
        log.info("Delete request for category with id: {}", catId);
        categoryService.deleteCategory(catId);
        return ResponseEntity.status(204).build();
    }

    @PatchMapping("{catId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable @Positive Long catId,
                                                      @RequestBody @Valid NewCategoryDto updateCategoryDto) {
        log.info("Update request for category with id: {}", catId);
        return new ResponseEntity<>(categoryService.updateCategory(catId, updateCategoryDto), HttpStatus.OK);
    }


}