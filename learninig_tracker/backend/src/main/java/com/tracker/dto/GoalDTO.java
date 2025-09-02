package com.tracker.dto;

import com.tracker.model.LearningItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalDTO {
    private UUID id;
    private String title;
    private String description;
    private UUID learningItemId;
    private String learningItemTitle;
    private LocalDate dueDate;
    private Boolean completed;
    private ZonedDateTime completedDate;
    private LearningItem.Priority priority;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    
    // Additional fields for frontend
    private Boolean isOverdue;
    private Long daysUntilDue;
    
    public void calculateDerivedFields() {
        if (dueDate != null) {
            LocalDate today = LocalDate.now();
            isOverdue = !completed && dueDate.isBefore(today);
            daysUntilDue = ChronoUnit.DAYS.between(today, dueDate);
        }
    }
}
