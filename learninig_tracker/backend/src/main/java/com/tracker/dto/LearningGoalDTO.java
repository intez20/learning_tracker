package com.tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LearningGoalDTO {
    private UUID id;
    private String title;
    private String description;
    private UUID projectId;
    private String projectName;
    private LocalDate dueDate;
    private Boolean completed;
    private LocalDate completedDate;
    private String priority;
    private boolean isOverdue;
    private long daysUntilDue;
}
