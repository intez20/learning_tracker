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
public class WeeklyMilestoneDTO {
    private UUID id;
    private String title;
    private String description;
    private UUID projectId;
    private String projectName;
    private LocalDate weekStartDate;
    private LocalDate weekEndDate;
    private Boolean completed;
    private LocalDate completedDate;
    private String plannedTasks;
    private String completedTasks;
    private String reflection;
    private int completionPercentage;
    private boolean isCurrent;
}
