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
public class ProjectDTO {
    private UUID id;
    private String name;
    private String description;
    private String status;
    private String priority;
    private UUID categoryId;
    private String categoryName;
    private LocalDate startDate;
    private LocalDate estimatedCompletionDate;
    private LocalDate completionDate;
    private Long totalTimeSpent;
    private int resourceCount;
    private int goalCount;
    private int completedGoalCount;
    private double completionPercentage;
    private LocalDate lastActivity;
}
