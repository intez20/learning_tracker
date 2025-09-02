package com.tracker.controller;

import com.tracker.dto.GoalDTO;
import com.tracker.service.GoalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
@Tag(name = "Goals", description = "Goal management endpoints")
public class GoalController {
    
    private final GoalService goalService;
    
    @GetMapping
    @Operation(summary = "Get all goals")
    public ResponseEntity<List<GoalDTO>> getAllGoals() {
        return ResponseEntity.ok(goalService.getAllGoals());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get goal by ID")
    public ResponseEntity<GoalDTO> getGoalById(@PathVariable UUID id) {
        return goalService.getGoalById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/learning-item/{learningItemId}")
    @Operation(summary = "Get goals by learning item")
    public ResponseEntity<List<GoalDTO>> getGoalsByLearningItem(@PathVariable UUID learningItemId) {
        return ResponseEntity.ok(goalService.getGoalsByLearningItem(learningItemId));
    }
    
    @GetMapping("/learning-item/{learningItemId}/completed/{completed}")
    @Operation(summary = "Get goals by learning item and completion status")
    public ResponseEntity<List<GoalDTO>> getGoalsByLearningItemAndCompleted(
            @PathVariable UUID learningItemId, 
            @PathVariable Boolean completed) {
        return ResponseEntity.ok(goalService.getGoalsByLearningItemAndCompleted(learningItemId, completed));
    }
    
    @GetMapping("/overdue")
    @Operation(summary = "Get overdue goals")
    public ResponseEntity<List<GoalDTO>> getOverdueGoals() {
        return ResponseEntity.ok(goalService.getOverdueGoals());
    }
    
    @PostMapping
    @Operation(summary = "Create a new goal")
    public ResponseEntity<GoalDTO> createGoal(@RequestBody GoalDTO goalDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(goalService.createGoal(goalDTO));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update a goal")
    public ResponseEntity<GoalDTO> updateGoal(
            @PathVariable UUID id, 
            @RequestBody GoalDTO goalDTO) {
        return goalService.updateGoal(id, goalDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}/complete")
    @Operation(summary = "Mark a goal as completed")
    public ResponseEntity<GoalDTO> completeGoal(@PathVariable UUID id) {
        return goalService.completeGoal(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a goal")
    public ResponseEntity<Void> deleteGoal(@PathVariable UUID id) {
        return goalService.deleteGoal(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
