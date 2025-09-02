package com.tracker.service;

import com.tracker.dto.analytics.*;
import com.tracker.model.*;
import com.tracker.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    @Autowired
    private LearningItemRepository learningItemRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private ProgressEntryRepository progressEntryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Get overview analytics data
     */
    public AnalyticsOverviewDTO getOverview() {
        AnalyticsOverviewDTO overview = new AnalyticsOverviewDTO();

        try {
            // Get goals data
            List<Goal> goals = goalRepository.findAll();
            overview.setTotalGoals(goals.size());
            overview.setCompletedGoals((int) goals.stream()
                    .filter(g -> g.getCompleted() != null && g.getCompleted())
                    .count());

            // Get learning items data
            List<LearningItem> learningItems = learningItemRepository.findAll();
            overview.setTotalProjects(learningItems.size());
            overview.setCompletedProjects((int) learningItems.stream()
                    .filter(item -> item.getStatus() == LearningItem.LearningItemStatus.COMPLETED)
                    .count());

            // Calculate total hours from progress entries
            List<ProgressEntry> entries = progressEntryRepository.findAll();
            int totalMinutes = entries.stream()
                    .filter(entry -> entry.getMinutesSpent() != null)
                    .mapToInt(ProgressEntry::getMinutesSpent)
                    .sum();
            overview.setTotalHours(totalMinutes / 60);

            // Calculate average session time
            overview.setAvgSessionTime(entries.isEmpty() ? 0 : totalMinutes / entries.size());

            // Calculate streak days (consecutive days with entries)
            overview.setStreakDays(calculateCurrentStreak(entries));
        } catch (Exception e) {
            System.err.println("Error calculating analytics overview: " + e.getMessage());
            e.printStackTrace();
            
            // Set default values in case of error
            overview.setTotalGoals(0);
            overview.setCompletedGoals(0);
            overview.setTotalProjects(0);
            overview.setCompletedProjects(0);
            overview.setTotalHours(0);
            overview.setAvgSessionTime(0);
            overview.setStreakDays(0);
        }

        return overview;
    }

    /**
     * Get progress data for charts
     */
    public AnalyticsProgressDTO getProgressData(String timeRange) {
        AnalyticsProgressDTO progressData = new AnalyticsProgressDTO();
        
        try {
            // Parse the time range - handle invalid input
            LocalDate startDate = parseTimeRange(timeRange);
            LocalDate endDate = LocalDate.now();
            
            // Ensure startDate is not after endDate
            if (startDate.isAfter(endDate)) {
                startDate = endDate.minusDays(30); // Default to 30 days
            }

            // Get weekly progress
            progressData.setWeeklyProgress(getWeeklyProgress(startDate, endDate));
            
            // Get category breakdown
            progressData.setCategoryBreakdown(getCategoryBreakdown(startDate, endDate));
            
            // Get monthly goals progress
            progressData.setMonthlyGoals(getMonthlyGoals(startDate, endDate));
        } catch (Exception e) {
            System.err.println("Error calculating progress data: " + e.getMessage());
            e.printStackTrace();
            
            // Set default empty lists in case of error
            progressData.setWeeklyProgress(new ArrayList<>());
            progressData.setCategoryBreakdown(new ArrayList<>());
            progressData.setMonthlyGoals(new ArrayList<>());
        }
        
        return progressData;
    }

    /**
     * Get recent activity data
     */
    public List<ActivityDTO> getRecentActivity(int limit) {
        List<ActivityDTO> activities = new ArrayList<>();
        
        try {
            // Ensure limit is positive
            limit = Math.max(1, limit);
            
            // Get recent goal completions
            List<Goal> completedGoals = goalRepository.findByLearningItemAndCompleted(null, true);
            if (!completedGoals.isEmpty()) {
                for (Goal goal : completedGoals.subList(0, Math.min(completedGoals.size(), limit))) {
                    if (goal.getTitle() != null && goal.getUpdatedAt() != null) {
                        activities.add(new ActivityDTO(
                            1L, // Using a generic ID since UUID can't be directly converted to Long
                            "GOAL_COMPLETED",
                            "Completed \"" + goal.getTitle() + "\" goal",
                            goal.getUpdatedAt().toLocalDateTime()
                        ));
                    }
                }
            }
            
            // Get recently created goals
            List<Goal> recentGoals = goalRepository.findAll().stream()
                .filter(g -> g.getCreatedAt() != null)
                .sorted(Comparator.comparing(Goal::getCreatedAt).reversed())
                .collect(Collectors.toList());
                
            if (!recentGoals.isEmpty()) {
                for (Goal goal : recentGoals.subList(0, Math.min(recentGoals.size(), limit))) {
                    if ((goal.getCompleted() == null || !goal.getCompleted()) && goal.getTitle() != null && goal.getCreatedAt() != null) {
                        activities.add(new ActivityDTO(
                            2L, // Using a generic ID since UUID can't be directly converted to Long
                            "GOAL_CREATED",
                            "Set new goal \"" + goal.getTitle() + "\"",
                            goal.getCreatedAt().toLocalDateTime()
                        ));
                    }
                }
            }
            
            // Get recent progress entries
            List<ProgressEntry> recentEntries = progressEntryRepository.findAll().stream()
                .filter(e -> e.getDate() != null && e.getCreatedAt() != null)
                .sorted(Comparator.comparing(ProgressEntry::getDate).reversed())
                .collect(Collectors.toList());
                
            if (!recentEntries.isEmpty()) {
                for (ProgressEntry entry : recentEntries.subList(0, Math.min(recentEntries.size(), limit))) {
                    if (entry.getMinutesSpent() != null) {
                        activities.add(new ActivityDTO(
                            3L, // Using a generic ID since UUID can't be directly converted to Long
                            "HOURS_LOGGED",
                            "Logged " + (entry.getMinutesSpent() / 60) + " hours of study time",
                            entry.getCreatedAt().toLocalDateTime()
                        ));
                    }
                }
            }
            
            // Get recently started projects
            List<LearningItem> recentItems = learningItemRepository.findByStatus(LearningItem.LearningItemStatus.IN_PROGRESS);
            if (!recentItems.isEmpty()) {
                for (LearningItem item : recentItems.subList(0, Math.min(recentItems.size(), limit))) {
                    if (item.getTitle() != null && item.getCreatedAt() != null) {
                        activities.add(new ActivityDTO(
                            4L, // Using a generic ID since UUID can't be directly converted to Long
                            "PROJECT_STARTED",
                            "Started new project \"" + item.getTitle() + "\"",
                            item.getCreatedAt().toLocalDateTime()
                        ));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting recent activity: " + e.getMessage());
            e.printStackTrace();
            // Return empty list in case of error
            return new ArrayList<>();
        }
        
        // If no activities found, return empty list
        if (activities.isEmpty()) {
            return activities;
        }
        
        // Sort by timestamp descending and limit
        return activities.stream()
            .sorted(Comparator.comparing(ActivityDTO::getTimestamp).reversed())
            .limit(limit)
            .collect(Collectors.toList());
    }

    /**
     * Get learning insights
     */
    public AnalyticsInsightsDTO getInsights() {
        AnalyticsInsightsDTO insights = new AnalyticsInsightsDTO();
        
        try {
            // Calculate most productive day
            AnalyticsInsightsDTO.MostProductiveDayDTO mostProductiveDay = calculateMostProductiveDay();
            insights.setMostProductiveDay(mostProductiveDay);
            
            // Calculate goal completion rate
            insights.setGoalCompletionRate(calculateGoalCompletionRate());
            
            // Calculate longest streak
            insights.setLongestStreak(calculateLongestStreak());
        } catch (Exception e) {
            System.err.println("Error calculating learning insights: " + e.getMessage());
            e.printStackTrace();
            
            // Set default values in case of error
            insights.setMostProductiveDay(new AnalyticsInsightsDTO.MostProductiveDayDTO("Monday", 0.0));
            insights.setGoalCompletionRate(0);
            insights.setLongestStreak(0);
        }
        
        return insights;
    }

    // Helper methods
    
    private LocalDate parseTimeRange(String timeRange) {
        LocalDate now = LocalDate.now();
        
        if (timeRange.equals("7d")) {
            return now.minusDays(7);
        } else if (timeRange.equals("30d")) {
            return now.minusDays(30);
        } else if (timeRange.equals("90d")) {
            return now.minusDays(90);
        } else if (timeRange.equals("1y")) {
            return now.minusYears(1);
        }
        
        // Default to 30 days
        return now.minusDays(30);
    }
    
    private List<AnalyticsProgressDTO.WeeklyProgressDTO> getWeeklyProgress(LocalDate startDate, LocalDate endDate) {
        List<AnalyticsProgressDTO.WeeklyProgressDTO> weeklyProgress = new ArrayList<>();
        
        try {
            if (startDate == null || endDate == null) {
                System.err.println("Warning: Null start or end date in getWeeklyProgress");
                // Default to last 30 days if dates are not provided
                endDate = LocalDate.now();
                startDate = endDate.minusDays(30);
            }
            
            // Group progress entries by week
            Map<String, Integer> weeklyHours = new HashMap<>();
            Map<String, Integer> weeklyGoals = new HashMap<>();
            
            // Get all progress entries in date range
            List<ProgressEntry> entries = progressEntryRepository.findByDateBetween(startDate, endDate);
            
            // Format for week identifier (e.g., "2025-W35")
            DateTimeFormatter weekFormatter = DateTimeFormatter.ofPattern("yyyy-'W'w");
            
            // Calculate weekly hours
            if (entries != null) {
                for (ProgressEntry entry : entries) {
                    if (entry.getDate() != null && entry.getMinutesSpent() != null) {
                        String weekKey = entry.getDate().format(weekFormatter);
                        weeklyHours.put(weekKey, 
                            weeklyHours.getOrDefault(weekKey, 0) + (entry.getMinutesSpent() / 60));
                    }
                }
            }
            
            // Calculate weekly goals completed
            List<Goal> completedGoals = new ArrayList<>();
            try {
                completedGoals = goalRepository.findAll().stream()
                    .filter(g -> g != null && g.getCompleted() != null && g.getCompleted() && g.getCompletedDate() != null)
                    .filter(g -> {
                        try {
                            LocalDate completionDate = g.getCompletedDate().toLocalDate();
                            return !completionDate.isBefore(startDate) && !completionDate.isAfter(endDate);
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .collect(Collectors.toList());
            } catch (Exception e) {
                System.err.println("Error fetching completed goals: " + e.getMessage());
            }
            
            for (Goal goal : completedGoals) {
                try {
                    String weekKey = goal.getCompletedDate().toLocalDate().format(weekFormatter);
                    weeklyGoals.put(weekKey, 
                        weeklyGoals.getOrDefault(weekKey, 0) + 1);
                } catch (Exception e) {
                    System.err.println("Error processing goal: " + e.getMessage());
                }
            }
            
            // Generate weekly data for the last 4 weeks
            LocalDate date = endDate;
            for (int i = 0; i < 4; i++) {
                try {
                    String weekKey = date.format(weekFormatter);
                    String weekLabel = "Week " + date.get(ChronoField.ALIGNED_WEEK_OF_YEAR);
                    
                    weeklyProgress.add(new AnalyticsProgressDTO.WeeklyProgressDTO(
                        weekLabel,
                        weeklyHours.getOrDefault(weekKey, 0),
                        weeklyGoals.getOrDefault(weekKey, 0)
                    ));
                    
                    date = date.minusWeeks(1);
                } catch (Exception e) {
                    System.err.println("Error generating weekly data for week " + i + ": " + e.getMessage());
                }
            }
            
            // Reverse to show oldest first
            Collections.reverse(weeklyProgress);
        } catch (Exception e) {
            System.err.println("Error calculating weekly progress: " + e.getMessage());
            e.printStackTrace();
            // Return empty list in case of error
            return new ArrayList<>();
        }
        
        return weeklyProgress;
    }
    
    private List<AnalyticsProgressDTO.CategoryBreakdownDTO> getCategoryBreakdown(LocalDate startDate, LocalDate endDate) {
        List<AnalyticsProgressDTO.CategoryBreakdownDTO> categoryBreakdown = new ArrayList<>();
        
        try {
            if (startDate == null || endDate == null) {
                System.err.println("Warning: Null start or end date in getCategoryBreakdown");
                // Default to last 30 days if dates are not provided
                endDate = LocalDate.now();
                startDate = endDate.minusDays(30);
            }
            
            // Get all progress entries in date range
            List<ProgressEntry> entries = progressEntryRepository.findByDateBetween(startDate, endDate);
            
            if (entries == null || entries.isEmpty()) {
                return categoryBreakdown;
            }
            
            // Group entries by category
            Map<String, Integer> categoryMinutes = new HashMap<>();
            int totalMinutes = 0;
            
            for (ProgressEntry entry : entries) {
                if (entry == null || entry.getMinutesSpent() == null) {
                    continue;
                }
                
                String categoryName = "Uncategorized";
                try {
                    Project project = entry.getProject();
                    if (project != null && project.getCategory() != null) {
                        categoryName = project.getCategory().getName();
                    }
                } catch (Exception e) {
                    // If there's an error accessing the category, default to Uncategorized
                    System.err.println("Error accessing category: " + e.getMessage());
                }
                
                categoryMinutes.put(categoryName, 
                    categoryMinutes.getOrDefault(categoryName, 0) + entry.getMinutesSpent());
                
                totalMinutes += entry.getMinutesSpent();
            }
            
            // Calculate percentages and create DTOs
            for (Map.Entry<String, Integer> entry : categoryMinutes.entrySet()) {
                try {
                    int percentage = totalMinutes > 0 ? 
                        (int) Math.round((entry.getValue() * 100.0) / totalMinutes) : 0;
                    
                    categoryBreakdown.add(new AnalyticsProgressDTO.CategoryBreakdownDTO(
                        entry.getKey(),
                        percentage,
                        entry.getValue() / 60
                    ));
                } catch (Exception e) {
                    System.err.println("Error calculating percentage for category " + entry.getKey() + ": " + e.getMessage());
                }
            }
            
            // Sort by hours descending
            categoryBreakdown.sort(Comparator.comparing(AnalyticsProgressDTO.CategoryBreakdownDTO::getHours).reversed());
        } catch (Exception e) {
            System.err.println("Error calculating category breakdown: " + e.getMessage());
            e.printStackTrace();
            // Return empty list in case of error
        }
        
        return categoryBreakdown;
    }
    
    private List<AnalyticsProgressDTO.MonthlyGoalDTO> getMonthlyGoals(LocalDate startDate, LocalDate endDate) {
        List<AnalyticsProgressDTO.MonthlyGoalDTO> monthlyGoals = new ArrayList<>();
        
        try {
            if (startDate == null || endDate == null) {
                System.err.println("Warning: Null start or end date in getMonthlyGoals");
                // Default to last 4 months if dates are not provided
                endDate = LocalDate.now();
                startDate = endDate.minusMonths(4);
            }
            
            // Generate monthly data for recent months
            LocalDate date = endDate;
            DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
            
            for (int i = 0; i < 4; i++) {
                try {
                    String monthKey = date.format(monthFormatter);
                    String monthLabel = date.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
                    
                    // Get first and last day of month
                    LocalDate monthStart = date.withDayOfMonth(1);
                    LocalDate monthEnd = date.withDayOfMonth(date.lengthOfMonth());
                    
                    // Adjust to respect the overall date range
                    if (monthStart.isBefore(startDate)) {
                        monthStart = startDate;
                    }
                    if (monthEnd.isAfter(endDate)) {
                        monthEnd = endDate;
                    }
                    
                    // Count total goals in this month
                    int totalGoals = 0;
                    int completedGoals = 0;
                    
                    try {
                        List<Goal> allGoals = goalRepository.findAll();
                        
                        if (allGoals != null) {
                            totalGoals = (int) allGoals.stream()
                                .filter(g -> g != null && g.getDueDate() != null && 
                                    !g.getDueDate().isBefore(monthStart) && 
                                    !g.getDueDate().isAfter(monthEnd))
                                .count();
                            
                            // Count completed goals in this month
                            completedGoals = (int) allGoals.stream()
                                .filter(g -> g != null && g.getCompleted() != null && g.getCompleted() && 
                                       g.getCompletedDate() != null &&
                                       !g.getCompletedDate().toLocalDate().isBefore(monthStart) && 
                                       !g.getCompletedDate().toLocalDate().isAfter(monthEnd))
                                .count();
                        }
                    } catch (Exception e) {
                        System.err.println("Error counting goals for month " + monthLabel + ": " + e.getMessage());
                    }
                    
                    monthlyGoals.add(new AnalyticsProgressDTO.MonthlyGoalDTO(
                        monthLabel,
                        completedGoals,
                        totalGoals
                    ));
                    
                    date = date.minusMonths(1);
                } catch (Exception e) {
                    System.err.println("Error processing month at index " + i + ": " + e.getMessage());
                    // Continue with next month
                    date = date.minusMonths(1);
                }
            }
            
            // Reverse to show oldest first
            Collections.reverse(monthlyGoals);
            
        } catch (Exception e) {
            System.err.println("Error calculating monthly goals: " + e.getMessage());
            e.printStackTrace();
            // Return empty list in case of error
        }
        
        return monthlyGoals;
    }
    
    private int calculateCurrentStreak(List<ProgressEntry> entries) {
        if (entries == null || entries.isEmpty()) {
            return 0;
        }
        
        try {
            // Group entries by date
            Map<LocalDate, List<ProgressEntry>> entriesByDate = entries.stream()
                .filter(e -> e.getDate() != null)
                .collect(Collectors.groupingBy(ProgressEntry::getDate));
            
            if (entriesByDate.isEmpty()) {
                return 0;
            }
            
            // Sort dates in descending order
            List<LocalDate> dates = new ArrayList<>(entriesByDate.keySet());
            Collections.sort(dates, Comparator.reverseOrder());
            
            // Check if most recent entry is from today or yesterday
            LocalDate today = LocalDate.now();
            LocalDate yesterday = today.minusDays(1);
            
            if (!dates.isEmpty() && !dates.get(0).equals(today) && !dates.get(0).equals(yesterday)) {
                // Streak is broken if the most recent entry is older than yesterday
                return 0;
            }
            
            // Count consecutive days
            int streak = 0;
            LocalDate expectedDate = dates.isEmpty() ? today : dates.get(0);
            
            for (LocalDate date : dates) {
                if (date.equals(expectedDate) || date.equals(expectedDate.minusDays(1))) {
                    streak++;
                    expectedDate = date.minusDays(1);
                } else {
                    break;
                }
            }
            
            return streak;
        } catch (Exception e) {
            System.err.println("Error calculating current streak: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
    
    private AnalyticsInsightsDTO.MostProductiveDayDTO calculateMostProductiveDay() {
        try {
            // Get all progress entries
            List<ProgressEntry> entries = progressEntryRepository.findAll();
            
            if (entries == null || entries.isEmpty()) {
                return new AnalyticsInsightsDTO.MostProductiveDayDTO(
                    DayOfWeek.MONDAY.getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                    0.0
                );
            }
            
            // Group by day of week and calculate total hours
            Map<DayOfWeek, Integer> minutesByDay = new HashMap<>();
            Map<DayOfWeek, Integer> countsByDay = new HashMap<>();
            
            for (ProgressEntry entry : entries) {
                try {
                    if (entry != null && entry.getDate() != null && entry.getMinutesSpent() != null) {
                        DayOfWeek dayOfWeek = entry.getDate().getDayOfWeek();
                        
                        minutesByDay.put(dayOfWeek, 
                            minutesByDay.getOrDefault(dayOfWeek, 0) + entry.getMinutesSpent());
                        
                        countsByDay.put(dayOfWeek,
                            countsByDay.getOrDefault(dayOfWeek, 0) + 1);
                    }
                } catch (Exception e) {
                    System.err.println("Error processing entry for most productive day: " + e.getMessage());
                    // Continue with next entry
                }
            }
            
            // Find day with highest average
            DayOfWeek mostProductiveDay = DayOfWeek.MONDAY; // Default
            double highestAverage = 0;
            
            for (DayOfWeek day : DayOfWeek.values()) {
                try {
                    if (countsByDay.containsKey(day) && countsByDay.get(day) > 0) {
                        double average = minutesByDay.get(day) / (double) countsByDay.get(day) / 60.0;
                        
                        if (average > highestAverage) {
                            highestAverage = average;
                            mostProductiveDay = day;
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error calculating average for day " + day + ": " + e.getMessage());
                    // Continue with next day
                }
            }
            
            return new AnalyticsInsightsDTO.MostProductiveDayDTO(
                mostProductiveDay.getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                Math.round(highestAverage * 10) / 10.0 // Round to 1 decimal place
            );
        } catch (Exception e) {
            System.err.println("Error calculating most productive day: " + e.getMessage());
            e.printStackTrace();
            return new AnalyticsInsightsDTO.MostProductiveDayDTO(
                DayOfWeek.MONDAY.getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                0.0
            );
        }
    }
    
    private int calculateGoalCompletionRate() {
        try {
            List<Goal> goals = goalRepository.findAll();
            
            if (goals == null || goals.isEmpty()) {
                return 0;
            }
            
            // Count completed goals with completion date before or on target date
            int onTimeCompletions = 0;
            int totalCompletedGoals = 0;
            
            for (Goal goal : goals) {
                try {
                    if (goal != null && goal.getCompleted() != null && goal.getCompleted()) {
                        totalCompletedGoals++;
                        
                        if (goal.getCompletedDate() != null && goal.getDueDate() != null) {
                            if (!goal.getCompletedDate().toLocalDate().isAfter(goal.getDueDate())) {
                                onTimeCompletions++;
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error processing goal for completion rate: " + e.getMessage());
                    // Continue with next goal
                }
            }
            
            return totalCompletedGoals > 0 ? 
                (int) Math.round((onTimeCompletions * 100.0) / totalCompletedGoals) : 0;
        } catch (Exception e) {
            System.err.println("Error calculating goal completion rate: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
    
    private int calculateLongestStreak() {
        try {
            // Get all progress entries
            List<ProgressEntry> entries = progressEntryRepository.findAll();
            
            if (entries == null || entries.isEmpty()) {
                return 0;
            }
            
            // Group entries by date
            Map<LocalDate, List<ProgressEntry>> entriesByDate = entries.stream()
                .filter(e -> e != null && e.getDate() != null)
                .collect(Collectors.groupingBy(ProgressEntry::getDate));
            
            if (entriesByDate.isEmpty()) {
                return 0;
            }
            
            // Sort dates in ascending order
            List<LocalDate> dates = new ArrayList<>(entriesByDate.keySet());
            if (dates.isEmpty()) {
                return 0;
            }
            
            Collections.sort(dates);
            
            int currentStreak = 1;
            int longestStreak = 1;
            
            for (int i = 1; i < dates.size(); i++) {
                // Check if this date is consecutive with the previous one
                if (dates.get(i).minusDays(1).equals(dates.get(i-1))) {
                    currentStreak++;
                    longestStreak = Math.max(longestStreak, currentStreak);
                } else {
                    currentStreak = 1;
                }
            }
            
            return longestStreak;
        } catch (Exception e) {
            System.err.println("Error calculating longest streak: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
}
