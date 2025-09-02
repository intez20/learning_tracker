package com.tracker.controller;

import com.tracker.dto.ItemProgressEntryDTO;
import com.tracker.service.ItemProgressEntryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/progress-entries")
@RequiredArgsConstructor
@Tag(name = "Progress Entries", description = "Progress entry management endpoints")
public class ItemProgressEntryController {
    
    private final ItemProgressEntryService progressEntryService;
    
    @GetMapping
    @Operation(summary = "Get all progress entries")
    public ResponseEntity<List<ItemProgressEntryDTO>> getAllProgressEntries() {
        return ResponseEntity.ok(progressEntryService.getAllProgressEntries());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get progress entry by ID")
    public ResponseEntity<ItemProgressEntryDTO> getProgressEntryById(@PathVariable UUID id) {
        return progressEntryService.getProgressEntryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/learning-item/{learningItemId}")
    @Operation(summary = "Get progress entries by learning item")
    public ResponseEntity<List<ItemProgressEntryDTO>> getProgressEntriesByLearningItem(@PathVariable UUID learningItemId) {
        return ResponseEntity.ok(progressEntryService.getProgressEntriesByLearningItem(learningItemId));
    }
    
    @GetMapping("/date-range")
    @Operation(summary = "Get progress entries by date range")
    public ResponseEntity<List<ItemProgressEntryDTO>> getProgressEntriesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(progressEntryService.getProgressEntriesByDateRange(startDate, endDate));
    }
    
    @PostMapping
    @Operation(summary = "Create a new progress entry")
    public ResponseEntity<ItemProgressEntryDTO> createProgressEntry(@RequestBody ItemProgressEntryDTO entryDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(progressEntryService.createProgressEntry(entryDTO));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update a progress entry")
    public ResponseEntity<ItemProgressEntryDTO> updateProgressEntry(
            @PathVariable UUID id, 
            @RequestBody ItemProgressEntryDTO entryDTO) {
        return progressEntryService.updateProgressEntry(id, entryDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a progress entry")
    public ResponseEntity<Void> deleteProgressEntry(@PathVariable UUID id) {
        return progressEntryService.deleteProgressEntry(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
