package com.tracker.controller;

import com.tracker.dto.ProjectDTO;
import com.tracker.mapper.EntityMapper;
import com.tracker.model.Category;
import com.tracker.model.Project;
import com.tracker.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "Projects", description = "Project management endpoints")
public class ProjectController {

    private final ProjectService projectService;
    private final CategoryService categoryService;
    private final ProgressEntryService progressEntryService;
    private final LearningResourceService learningResourceService;
    private final LearningGoalService learningGoalService;
    private final EntityMapper entityMapper;

    @GetMapping
    @Operation(summary = "Get all projects")
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        List<ProjectDTO> projectDTOs = projects.stream()
                .map(this::convertToProjectDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(projectDTOs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project by ID")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable UUID id) {
        return projectService.getProjectById(id)
                .map(project -> ResponseEntity.ok(convertToProjectDTO(project)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new project")
    public ResponseEntity<ProjectDTO> createProject(@RequestBody Project project) {
        Project savedProject = projectService.createProject(project);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertToProjectDTO(savedProject));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing project")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable UUID id, @RequestBody Project project) {
        return projectService.updateProject(id, project)
                .map(updatedProject -> ResponseEntity.ok(convertToProjectDTO(updatedProject)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a project")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID id) {
        if (projectService.deleteProject(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get projects by category")
    public ResponseEntity<List<ProjectDTO>> getProjectsByCategory(@PathVariable UUID categoryId) {
        List<Project> projects = projectService.getProjectsByCategory(categoryId);
        List<ProjectDTO> projectDTOs = projects.stream()
                .map(this::convertToProjectDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(projectDTOs);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get projects by status")
    public ResponseEntity<List<ProjectDTO>> getProjectsByStatus(@PathVariable String status) {
        List<Project> projects = projectService.getProjectsByStatus(status);
        List<ProjectDTO> projectDTOs = projects.stream()
                .map(this::convertToProjectDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(projectDTOs);
    }

    @GetMapping("/priority/{priority}")
    @Operation(summary = "Get projects by priority")
    public ResponseEntity<List<ProjectDTO>> getProjectsByPriority(@PathVariable String priority) {
        List<Project> projects = projectService.getProjectsByPriority(priority);
        List<ProjectDTO> projectDTOs = projects.stream()
                .map(this::convertToProjectDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(projectDTOs);
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active projects")
    public ResponseEntity<List<ProjectDTO>> getActiveProjects() {
        List<Project> projects = projectService.getActiveProjects();
        List<ProjectDTO> projectDTOs = projects.stream()
                .map(this::convertToProjectDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(projectDTOs);
    }

    private ProjectDTO convertToProjectDTO(Project project) {
        // Get category name
        String categoryName = "";
        if (project.getCategoryId() != null) {
            Optional<Category> categoryOpt = categoryService.getCategoryById(project.getCategoryId());
            categoryName = categoryOpt.map(Category::getName).orElse("");
        }

        // Get time spent
        Long totalTimeSpent = progressEntryService.getTotalTimeSpentOnProject(project.getId());
        if (totalTimeSpent == null) {
            totalTimeSpent = 0L;
        }

        // Get resource count
        int resourceCount = learningResourceService.getLearningResourcesByProject(project.getId()).size();

        // Get goal counts
        List<com.tracker.model.LearningGoal> goals = learningGoalService.getLearningGoalsByProject(project.getId());
        int goalCount = goals.size();
        int completedGoalCount = (int) goals.stream().filter(com.tracker.model.LearningGoal::getCompleted).count();

        // Get last activity date
        LocalDate lastActivity = LocalDate.now();
        // This would typically come from the most recent progress entry, but for simplicity we're using current date

        return entityMapper.mapToProjectDTO(
                project, 
                totalTimeSpent, 
                resourceCount, 
                goalCount, 
                completedGoalCount, 
                categoryName, 
                lastActivity
        );
    }
}
