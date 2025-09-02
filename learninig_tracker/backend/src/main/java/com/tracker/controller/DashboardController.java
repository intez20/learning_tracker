package com.tracker.controller;

import com.tracker.dto.*;
import com.tracker.mapper.EntityMapper;
import com.tracker.model.*;
import com.tracker.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Dashboard statistics endpoints")
public class DashboardController {

    private final DashboardService dashboardService;
    private final CategoryService categoryService;
    private final ProjectService projectService;
    private final ProgressEntryService progressEntryService;
    private final LearningGoalService learningGoalService;
    private final EntityMapper entityMapper;

    @GetMapping("/stats")
    @Operation(summary = "Get dashboard statistics")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        // Get basic counts
        long totalProjects = projectService.getAllProjects().size();
        long activeProjects = projectService.getActiveProjects().size();
        long completedProjects = totalProjects - activeProjects;
        long totalCategories = categoryService.getAllCategories().size();
        
        // Get learning time stats
        LocalDate now = LocalDate.now();
        LocalDate weekStart = now.minusDays(7);
        LocalDate monthStart = now.minusMonths(1);
        
        Long totalLearningTime = progressEntryService.getTotalTimeSpentInDateRange(
                LocalDate.of(2000, 1, 1), now);
        Long learningTimeThisWeek = progressEntryService.getTotalTimeSpentInDateRange(weekStart, now);
        Long learningTimeThisMonth = progressEntryService.getTotalTimeSpentInDateRange(monthStart, now);
        
        if (totalLearningTime == null) totalLearningTime = 0L;
        if (learningTimeThisWeek == null) learningTimeThisWeek = 0L;
        if (learningTimeThisMonth == null) learningTimeThisMonth = 0L;
        
        // Get streak data
        List<LocalDate> streakDays = dashboardService.getStreakDays();
        int currentStreak = calculateCurrentStreak(streakDays);
        int longestStreak = calculateLongestStreak(streakDays);
        
        // Get high priority goals
        long incompleteHighPriorityGoals = dashboardService.getCountOfIncompleteHighPriorityGoals();
        
        // Get time spent by category
        Map<String, Long> timeSpentByCategory = new HashMap<>();
        dashboardService.getTimeSpentByCategory().forEach(result -> 
            timeSpentByCategory.put((String) result[0], (Long) result[1]));
        
        // Get time spent by project
        Map<String, Long> timeSpentByProject = new HashMap<>();
        dashboardService.getTimeSpentByProject().forEach(result -> 
            timeSpentByProject.put((String) result[0], (Long) result[1]));
        
        // Get top projects
        List<Project> projects = projectService.getAllProjects();
        List<ProjectDTO> topProjects = projects.stream()
                .limit(5)
                .map(project -> {
                    // This is simplified - in a real app you'd sort by activity or other metrics
                    return convertToProjectDTO(project);
                })
                .collect(Collectors.toList());
        
        // Get categories
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(category -> {
                    int projectCount = projectService.getProjectsByCategory(category.getId()).size();
                    return entityMapper.mapToCategoryDTO(category, projectCount);
                })
                .collect(Collectors.toList());
        
        // Get upcoming goals
        List<LearningGoal> upcomingGoalsEntities = learningGoalService.getUpcomingGoals(now.plusDays(14));
        List<LearningGoalDTO> upcomingGoals = upcomingGoalsEntities.stream()
                .map(goal -> {
                    String projectName = projectService.getProjectById(goal.getProjectId())
                            .map(Project::getName)
                            .orElse("");
                    return entityMapper.mapToLearningGoalDTO(goal, projectName);
                })
                .collect(Collectors.toList());
        
        // Get recent entries
        List<ProgressEntry> recentEntries = progressEntryService
                .getProgressEntriesInDateRangeOrderByDate(now.minusDays(30), now);
        List<ProgressEntryDTO> recentEntriesDTO = recentEntries.stream()
                .map(entry -> {
                    String projectName = projectService.getProjectById(entry.getProjectId())
                            .map(Project::getName)
                            .orElse("");
                    return entityMapper.mapToProgressEntryDTO(entry, projectName);
                })
                .collect(Collectors.toList());
        
        // Build the DTO
        DashboardStatsDTO statsDTO = DashboardStatsDTO.builder()
                .totalProjects(totalProjects)
                .activeProjects(activeProjects)
                .completedProjects(completedProjects)
                .totalCategories(totalCategories)
                .totalLearningTime(totalLearningTime)
                .learningTimeThisWeek(learningTimeThisWeek)
                .learningTimeThisMonth(learningTimeThisMonth)
                .streakDays(streakDays)
                .currentStreak(currentStreak)
                .longestStreak(longestStreak)
                .incompleteHighPriorityGoals(incompleteHighPriorityGoals)
                .timeSpentByCategory(timeSpentByCategory)
                .timeSpentByProject(timeSpentByProject)
                .topProjects(topProjects)
                .categories(categoryDTOs)
                .upcomingGoals(upcomingGoals)
                .recentEntries(recentEntriesDTO)
                .build();
                
        return ResponseEntity.ok(statsDTO);
    }
    
    private int calculateCurrentStreak(List<LocalDate> streakDays) {
        if (streakDays == null || streakDays.isEmpty()) {
            return 0;
        }
        
        // Sort dates in descending order
        List<LocalDate> sortedDates = new ArrayList<>(streakDays);
        sortedDates.sort(Comparator.reverseOrder());
        
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        
        // Check if most recent date is today or yesterday
        if (!sortedDates.get(0).equals(today) && !sortedDates.get(0).equals(yesterday)) {
            return 0;
        }
        
        int streak = 1;
        LocalDate prevDate = sortedDates.get(0);
        
        for (int i = 1; i < sortedDates.size(); i++) {
            LocalDate currDate = sortedDates.get(i);
            
            // If dates are consecutive
            if (prevDate.minusDays(1).equals(currDate)) {
                streak++;
                prevDate = currDate;
            } else {
                break;
            }
        }
        
        return streak;
    }
    
    private int calculateLongestStreak(List<LocalDate> streakDays) {
        if (streakDays == null || streakDays.isEmpty()) {
            return 0;
        }
        
        // Sort dates in ascending order
        List<LocalDate> sortedDates = new ArrayList<>(streakDays);
        sortedDates.sort(Comparator.naturalOrder());
        
        int longestStreak = 1;
        int currentStreak = 1;
        LocalDate prevDate = sortedDates.get(0);
        
        for (int i = 1; i < sortedDates.size(); i++) {
            LocalDate currDate = sortedDates.get(i);
            
            // If dates are consecutive
            if (prevDate.plusDays(1).equals(currDate)) {
                currentStreak++;
            } else {
                currentStreak = 1;
            }
            
            longestStreak = Math.max(longestStreak, currentStreak);
            prevDate = currDate;
        }
        
        return longestStreak;
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
        int resourceCount = 0; // In a real app, you'd get this from the service

        // Get goal counts
        List<com.tracker.model.LearningGoal> goals = learningGoalService.getLearningGoalsByProject(project.getId());
        int goalCount = goals.size();
        int completedGoalCount = (int) goals.stream().filter(com.tracker.model.LearningGoal::getCompleted).count();

        // Get last activity date
        LocalDate lastActivity = LocalDate.now();

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
