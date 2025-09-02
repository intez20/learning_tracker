package com.tracker.mapper;

import com.tracker.dto.*;
import com.tracker.model.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EntityMapper {

    public CategoryDTO mapToCategoryDTO(Category category, int projectCount) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .color(category.getColor())
                .projectCount(projectCount)
                .build();
    }

    public ProjectDTO mapToProjectDTO(Project project, Long totalTimeSpent, int resourceCount, 
                                     int goalCount, int completedGoalCount, String categoryName, 
                                     LocalDate lastActivity) {
        double completionPercentage = 0;
        if (goalCount > 0) {
            completionPercentage = ((double) completedGoalCount / goalCount) * 100;
        }
        
        return ProjectDTO.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .status(project.getStatus())
                .priority(project.getPriority())
                .categoryId(project.getCategoryId())
                .categoryName(categoryName)
                .startDate(project.getStartDate())
                .estimatedCompletionDate(project.getEstimatedCompletionDate())
                .completionDate(project.getCompletionDate())
                .totalTimeSpent(totalTimeSpent)
                .resourceCount(resourceCount)
                .goalCount(goalCount)
                .completedGoalCount(completedGoalCount)
                .completionPercentage(completionPercentage)
                .lastActivity(lastActivity)
                .build();
    }

    public LearningResourceDTO mapToLearningResourceDTO(LearningResource resource, String projectName) {
        return LearningResourceDTO.builder()
                .id(resource.getId())
                .title(resource.getTitle())
                .url(resource.getUrl())
                .description(resource.getDescription())
                .resourceType(resource.getResourceType())
                .author(resource.getAuthor())
                .projectId(resource.getProjectId())
                .projectName(projectName)
                .isPrimary(resource.getIsPrimary())
                .notes(resource.getNotes())
                .build();
    }

    public ProgressEntryDTO mapToProgressEntryDTO(ProgressEntry entry, String projectName) {
        return ProgressEntryDTO.builder()
                .id(entry.getId())
                .date(entry.getDate())
                .minutesSpent(entry.getMinutesSpent())
                .description(entry.getDescription())
                .projectId(entry.getProjectId())
                .projectName(projectName)
                .challenges(entry.getChallenges())
                .learnings(entry.getLearnings())
                .nextSteps(entry.getNextSteps())
                .mood(entry.getMood())
                .build();
    }

    public LearningGoalDTO mapToLearningGoalDTO(LearningGoal goal, String projectName) {
        LocalDate now = LocalDate.now();
        boolean isOverdue = !goal.getCompleted() && goal.getDueDate() != null && goal.getDueDate().isBefore(now);
        long daysUntilDue = goal.getDueDate() != null ? ChronoUnit.DAYS.between(now, goal.getDueDate()) : 0;
        
        return LearningGoalDTO.builder()
                .id(goal.getId())
                .title(goal.getTitle())
                .description(goal.getDescription())
                .projectId(goal.getProjectId())
                .projectName(projectName)
                .dueDate(goal.getDueDate())
                .completed(goal.getCompleted())
                .completedDate(goal.getCompletedDate())
                .priority(goal.getPriority())
                .isOverdue(isOverdue)
                .daysUntilDue(daysUntilDue)
                .build();
    }

    public CodeSnippetDTO mapToCodeSnippetDTO(CodeSnippet snippet, String projectName) {
        String tagsString = snippet.getTags() != null ? String.join(", ", snippet.getTags()) : null;
        return CodeSnippetDTO.builder()
                .id(snippet.getId())
                .title(snippet.getTitle())
                .description(snippet.getDescription())
                .code(snippet.getCode())
                .language(snippet.getLanguage())
                .tags(tagsString)
                .projectId(snippet.getProjectId())
                .projectName(projectName)
                .build();
    }

    public WeeklyMilestoneDTO mapToWeeklyMilestoneDTO(WeeklyMilestone milestone, String projectName) {
        LocalDate now = LocalDate.now();
        boolean isCurrent = now.isAfter(milestone.getWeekStartDate()) && now.isBefore(milestone.getWeekEndDate());
        
        // Calculate completion percentage based on tasks
        int completionPercentage = 0;
        if (milestone.getCompleted()) {
            completionPercentage = 100;
        } else if (milestone.getPlannedTasks() != null && milestone.getCompletedTasks() != null) {
            // Simple calculation based on number of tasks
            // In a real app, would parse and count tasks more accurately
            String[] plannedTasksArray = milestone.getPlannedTasks().split("\n");
            String[] completedTasksArray = milestone.getCompletedTasks().split("\n");
            
            if (plannedTasksArray.length > 0) {
                completionPercentage = (completedTasksArray.length * 100) / plannedTasksArray.length;
            }
        }
        
        return WeeklyMilestoneDTO.builder()
                .id(milestone.getId())
                .title(milestone.getTitle())
                .description(milestone.getDescription())
                .projectId(milestone.getProjectId())
                .projectName(projectName)
                .weekStartDate(milestone.getWeekStartDate())
                .weekEndDate(milestone.getWeekEndDate())
                .completed(milestone.getCompleted())
                .completedDate(milestone.getCompletedDate())
                .plannedTasks(milestone.getPlannedTasks())
                .completedTasks(milestone.getCompletedTasks())
                .reflection(milestone.getReflection())
                .completionPercentage(completionPercentage)
                .isCurrent(isCurrent)
                .build();
    }
    
    // List mapping methods
    public List<CategoryDTO> mapToCategoryDTOList(List<Category> categories, List<Integer> projectCounts) {
        if (categories.size() != projectCounts.size()) {
            throw new IllegalArgumentException("Categories and project counts must be the same size");
        }
        
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(category -> mapToCategoryDTO(category, 0))
                .collect(Collectors.toList());
        
        for (int i = 0; i < categoryDTOs.size(); i++) {
            categoryDTOs.get(i).setProjectCount(projectCounts.get(i));
        }
        
        return categoryDTOs;
    }
    
    // New methods for LearningItem mapping
    public LearningItemDTO mapToLearningItemDTO(LearningItem learningItem) {
        LearningItemDTO dto = new LearningItemDTO();
        dto.setId(learningItem.getId());
        dto.setTitle(learningItem.getTitle());
        dto.setDescription(learningItem.getDescription());
        dto.setStatus(learningItem.getStatus());
        dto.setPriority(learningItem.getPriority());
        dto.setProgressPercentage(learningItem.getProgressPercentage());
        dto.setTotalHours(learningItem.getTotalHours());
        dto.setTargetDate(learningItem.getTargetDate());
        dto.setNotes(learningItem.getNotes());
        dto.setCreatedAt(learningItem.getCreatedAt());
        dto.setUpdatedAt(learningItem.getUpdatedAt());
        
        // Default values for additional fields
        dto.setResourceCount(0);
        dto.setGoalCount(0);
        dto.setCompletedGoalCount(0);
        
        return dto;
    }
    
    // Added methods for Goal mapping
    public GoalDTO mapToGoalDTO(Goal goal) {
        GoalDTO dto = new GoalDTO();
        dto.setId(goal.getId());
        dto.setTitle(goal.getTitle());
        dto.setDescription(goal.getDescription());
        
        if (goal.getLearningItem() != null) {
            dto.setLearningItemId(goal.getLearningItem().getId());
            dto.setLearningItemTitle(goal.getLearningItem().getTitle());
        }
        
        dto.setDueDate(goal.getDueDate());
        dto.setCompleted(goal.getCompleted());
        dto.setCompletedDate(goal.getCompletedDate());
        dto.setPriority(goal.getPriority());
        dto.setCreatedAt(goal.getCreatedAt());
        dto.setUpdatedAt(goal.getUpdatedAt());
        
        // Calculate derived fields
        if (goal.getDueDate() != null) {
            LocalDate today = LocalDate.now();
            dto.setIsOverdue(!goal.getCompleted() && goal.getDueDate().isBefore(today));
            dto.setDaysUntilDue(ChronoUnit.DAYS.between(today, goal.getDueDate()));
        } else {
            dto.setIsOverdue(false);
            dto.setDaysUntilDue(null);
        }
        
        return dto;
    }
    
    // Added methods for ItemProgressEntry mapping
    public ItemProgressEntryDTO mapToItemProgressEntryDTO(ItemProgressEntry entry) {
        ItemProgressEntryDTO dto = new ItemProgressEntryDTO();
        dto.setId(entry.getId());
        
        if (entry.getLearningItem() != null) {
            dto.setLearningItemId(entry.getLearningItem().getId());
            dto.setLearningItemTitle(entry.getLearningItem().getTitle());
        }
        
        dto.setDate(entry.getDate());
        dto.setHoursSpent(entry.getHoursSpent());
        dto.setProgress(entry.getProgress());
        dto.setNotes(entry.getNotes());
        dto.setCreatedAt(entry.getCreatedAt());
        dto.setUpdatedAt(entry.getUpdatedAt());
        
        return dto;
    }
}
