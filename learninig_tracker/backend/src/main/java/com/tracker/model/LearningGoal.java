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
@Table(name = "learning_goals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LearningGoal {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    private Project project;
    
    // Add projectId field that service expects
    @Column(name = "project_id", insertable = false, updatable = false)
    private UUID projectId;
    
    @Column(nullable = false)
    private String title;
    
    private String description;
    
    @Column(name = "goal_type")
    private String goalType;
    
    @Column(name = "target_value", precision = 10, scale = 2, nullable = false)
    private BigDecimal targetValue;
    
    @Column(name = "current_value", precision = 10, scale = 2)
    private BigDecimal currentValue = BigDecimal.ZERO;
    
    @Column(nullable = false)
    private String unit;
    
    private LocalDate deadline;
    
    // Add dueDate alias that service expects
    @Column(name = "due_date")
    private LocalDate dueDate;
    
    @Column(name = "is_completed")
    private Boolean isCompleted = false;
    
    // Add completed alias for service
    @Column(name = "completed")
    private Boolean completed = false;
    
    @Column(name = "completed_at")
    private ZonedDateTime completedAt;
    
    // Add completedDate alias that service expects
    @Column(name = "completed_date")
    private LocalDate completedDate;
    
    private String priority = "medium";
    
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
        
        // If goal is completed and completedAt is not set, set it
        if (Boolean.TRUE.equals(isCompleted) && completedAt == null) {
            completedAt = ZonedDateTime.now();
        }
    }
}
