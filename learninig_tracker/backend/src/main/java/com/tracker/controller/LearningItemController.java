package com.tracker.controller;

import com.tracker.dto.LearningItemDTO;
import com.tracker.model.LearningItem;
import com.tracker.service.LearningItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/learning-items")
@RequiredArgsConstructor
@Tag(name = "Learning Items", description = "Learning item management endpoints")
public class LearningItemController {
    
    private final LearningItemService learningItemService;
    
    @GetMapping
    @Operation(summary = "Get all learning items")
    public ResponseEntity<List<LearningItemDTO>> getAllLearningItems() {
        return ResponseEntity.ok(learningItemService.getAllLearningItems());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get learning item by ID")
    public ResponseEntity<LearningItemDTO> getLearningItemById(@PathVariable UUID id) {
        return learningItemService.getLearningItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get learning items by category")
    public ResponseEntity<List<LearningItemDTO>> getLearningItemsByCategory(@PathVariable UUID categoryId) {
        return ResponseEntity.ok(learningItemService.getLearningItemsByCategory(categoryId));
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "Get learning items by status")
    public ResponseEntity<List<LearningItemDTO>> getLearningItemsByStatus(
            @PathVariable LearningItem.LearningItemStatus status) {
        return ResponseEntity.ok(learningItemService.getLearningItemsByStatus(status));
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search learning items by title or description")
    public ResponseEntity<List<LearningItemDTO>> searchLearningItems(@RequestParam String searchTerm) {
        return ResponseEntity.ok(learningItemService.searchLearningItems(searchTerm));
    }
    
    @PostMapping
    @Operation(summary = "Create a new learning item")
    public ResponseEntity<LearningItemDTO> createLearningItem(@RequestBody LearningItemDTO learningItemDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(learningItemService.createLearningItem(learningItemDTO));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update a learning item")
    public ResponseEntity<LearningItemDTO> updateLearningItem(
            @PathVariable UUID id, 
            @RequestBody LearningItemDTO learningItemDTO) {
        return learningItemService.updateLearningItem(id, learningItemDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a learning item")
    public ResponseEntity<Void> deleteLearningItem(@PathVariable UUID id) {
        return learningItemService.deleteLearningItem(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
