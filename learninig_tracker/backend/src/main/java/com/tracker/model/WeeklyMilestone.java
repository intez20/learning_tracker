package com.tracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "weekly_milestones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyMilestone {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    // Add fields that services expect
    private String title;
    private String description;
    
    // Add projectId field that service expects
    @Column(name = "project_id")
    private UUID projectId;
    
    @Column(name = "week_number", nullable = false)
    private Integer weekNumber;
    
    // Add week date fields that service expects
    @Column(name = "week_start_date")
    private LocalDate weekStartDate;
    
    @Column(name = "week_end_date")
    private LocalDate weekEndDate;
    
    @Column(name = "focus_area", nullable = false)
    private String focusArea;
    
    @Column(nullable = false)
    private String topics;
    
    @Column(name = "target_hours_per_day", precision = 3, scale = 2)
    private BigDecimal targetHoursPerDay;
    
    @Column(name = "actual_hours_completed", precision = 5, scale = 2)
    private BigDecimal actualHoursCompleted = BigDecimal.ZERO;
    
    @Column(name = "completion_percentage")
    private Integer completionPercentage = 0;
    
    @Column(name = "is_completed")
    private Boolean isCompleted = false;
    
    // Add completed alias that service expects
    @Column(name = "completed")
    private Boolean completed = false;
    
    @Column(name = "completed_at")
    private ZonedDateTime completedAt;
    
    // Add completedDate alias that service expects
    @Column(name = "completed_date")
    private LocalDate completedDate;
    
    // Add task fields that service expects
    @Column(name = "planned_tasks")
    private String plannedTasks;
    
    @Column(name = "completed_tasks")
    private String completedTasks;
    
    // Add reflection field that service expects
    private String reflection;
    
    private String notes;
    
    @Column(name = "created_at")
    private ZonedDateTime createdAt;
    
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = ZonedDateTime.now();
        updatedAt = ZonedDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = ZonedDateTime.now();
        
        // If milestone is completed and completedAt is not set, set it
        if (Boolean.TRUE.equals(isCompleted) && completedAt == null) {
            completedAt = ZonedDateTime.now();
        }
    }
}
