package com.tracker.dto.analytics;

public class AnalyticsInsightsDTO {
    private MostProductiveDayDTO mostProductiveDay;
    private int goalCompletionRate;
    private int longestStreak;

    public AnalyticsInsightsDTO() {
    }

    public MostProductiveDayDTO getMostProductiveDay() {
        return mostProductiveDay;
    }

    public void setMostProductiveDay(MostProductiveDayDTO mostProductiveDay) {
        this.mostProductiveDay = mostProductiveDay;
    }

    public int getGoalCompletionRate() {
        return goalCompletionRate;
    }

    public void setGoalCompletionRate(int goalCompletionRate) {
        this.goalCompletionRate = goalCompletionRate;
    }

    public int getLongestStreak() {
        return longestStreak;
    }

    public void setLongestStreak(int longestStreak) {
        this.longestStreak = longestStreak;
    }

    public static class MostProductiveDayDTO {
        private String day;
        private double averageHours;

        public MostProductiveDayDTO() {
        }

        public MostProductiveDayDTO(String day, double averageHours) {
            this.day = day;
            this.averageHours = averageHours;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public double getAverageHours() {
            return averageHours;
        }

        public void setAverageHours(double averageHours) {
            this.averageHours = averageHours;
        }
    }
}
