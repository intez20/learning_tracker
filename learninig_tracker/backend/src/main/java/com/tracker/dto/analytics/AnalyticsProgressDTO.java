package com.tracker.dto.analytics;

import java.util.List;

public class AnalyticsProgressDTO {
    private List<WeeklyProgressDTO> weeklyProgress;
    private List<CategoryBreakdownDTO> categoryBreakdown;
    private List<MonthlyGoalDTO> monthlyGoals;

    public AnalyticsProgressDTO() {
    }

    public List<WeeklyProgressDTO> getWeeklyProgress() {
        return weeklyProgress;
    }

    public void setWeeklyProgress(List<WeeklyProgressDTO> weeklyProgress) {
        this.weeklyProgress = weeklyProgress;
    }

    public List<CategoryBreakdownDTO> getCategoryBreakdown() {
        return categoryBreakdown;
    }

    public void setCategoryBreakdown(List<CategoryBreakdownDTO> categoryBreakdown) {
        this.categoryBreakdown = categoryBreakdown;
    }

    public List<MonthlyGoalDTO> getMonthlyGoals() {
        return monthlyGoals;
    }

    public void setMonthlyGoals(List<MonthlyGoalDTO> monthlyGoals) {
        this.monthlyGoals = monthlyGoals;
    }

    // Inner DTOs
    public static class WeeklyProgressDTO {
        private String week;
        private int hours;
        private int goals;

        public WeeklyProgressDTO() {
        }

        public WeeklyProgressDTO(String week, int hours, int goals) {
            this.week = week;
            this.hours = hours;
            this.goals = goals;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public int getHours() {
            return hours;
        }

        public void setHours(int hours) {
            this.hours = hours;
        }

        public int getGoals() {
            return goals;
        }

        public void setGoals(int goals) {
            this.goals = goals;
        }
    }

    public static class CategoryBreakdownDTO {
        private String category;
        private int percentage;
        private int hours;

        public CategoryBreakdownDTO() {
        }

        public CategoryBreakdownDTO(String category, int percentage, int hours) {
            this.category = category;
            this.percentage = percentage;
            this.hours = hours;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public int getPercentage() {
            return percentage;
        }

        public void setPercentage(int percentage) {
            this.percentage = percentage;
        }

        public int getHours() {
            return hours;
        }

        public void setHours(int hours) {
            this.hours = hours;
        }
    }

    public static class MonthlyGoalDTO {
        private String month;
        private int completed;
        private int total;

        public MonthlyGoalDTO() {
        }

        public MonthlyGoalDTO(String month, int completed, int total) {
            this.month = month;
            this.completed = completed;
            this.total = total;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public int getCompleted() {
            return completed;
        }

        public void setCompleted(int completed) {
            this.completed = completed;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }
}
