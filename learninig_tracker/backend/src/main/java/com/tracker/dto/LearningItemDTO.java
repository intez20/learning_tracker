package com.tracker.dto;

import com.tracker.model.LearningItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LearningItemDTO {
    private UUID id;
    private String title;
    private String description;
    private LearningItem.LearningItemStatus status;
    private LearningItem.Priority priority;
    private UUID categoryId;
    private String categoryName;
    private Integer progressPercentage;
    private BigDecimal totalHours;
    private LocalDate targetDate;
    private String notes;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private Integer resourceCount;
    private Integer goalCount;
    private Integer completedGoalCount;
}
