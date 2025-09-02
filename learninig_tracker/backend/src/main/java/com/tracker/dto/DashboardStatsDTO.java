package com.tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private long totalProjects;
    private long activeProjects;
    private long completedProjects;
    private long totalCategories;
    private long totalLearningTime;
    private long learningTimeThisWeek;
    private long learningTimeThisMonth;
    private List<LocalDate> streakDays;
    private int currentStreak;
    private int longestStreak;
    private long incompleteHighPriorityGoals;
    private Map<String, Long> timeSpentByCategory;
    private Map<String, Long> timeSpentByProject;
    private List<ProjectDTO> topProjects;
    private List<CategoryDTO> categories;
    private List<LearningGoalDTO> upcomingGoals;
    private List<ProgressEntryDTO> recentEntries;
}
