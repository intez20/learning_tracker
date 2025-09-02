package com.tracker.controller;

import com.tracker.dto.CategoryDTO;
import com.tracker.mapper.EntityMapper;
import com.tracker.model.Category;
import com.tracker.service.CategoryService;
import com.tracker.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Category management endpoints")
public class CategoryController {

    private final CategoryService categoryService;
    private final ProjectService projectService;
    private final EntityMapper entityMapper;

    @GetMapping
    @Operation(summary = "Get all categories")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(category -> {
                    int projectCount = projectService.getProjectsByCategory(category.getId()).size();
                    return entityMapper.mapToCategoryDTO(category, projectCount);
                })
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(categoryDTOs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable UUID id) {
        return categoryService.getCategoryById(id)
                .map(category -> {
                    int projectCount = projectService.getProjectsByCategory(category.getId()).size();
                    return ResponseEntity.ok(entityMapper.mapToCategoryDTO(category, projectCount));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new category")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody Category category) {
        Category savedCategory = categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(entityMapper.mapToCategoryDTO(savedCategory, 0));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing category")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable UUID id, @RequestBody Category category) {
        return categoryService.updateCategory(id, category)
                .map(updatedCategory -> {
                    int projectCount = projectService.getProjectsByCategory(updatedCategory.getId()).size();
                    return ResponseEntity.ok(entityMapper.mapToCategoryDTO(updatedCategory, projectCount));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        if (categoryService.deleteCategory(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Search categories by name")
    public ResponseEntity<List<CategoryDTO>> searchCategories(@RequestParam String name) {
        List<Category> categories = categoryService.getCategoriesByName(name);
        
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(category -> {
                    int projectCount = projectService.getProjectsByCategory(category.getId()).size();
                    return entityMapper.mapToCategoryDTO(category, projectCount);
                })
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(categoryDTOs);
    }
}
