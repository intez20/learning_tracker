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
@Table(name = "learning_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LearningItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(nullable = false)
    private String title;
    
    private String description;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LearningItemStatus status = LearningItemStatus.NOT_STARTED;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Priority priority = Priority.MEDIUM;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    
    @Column(name = "progress_percentage")
    private Integer progressPercentage = 0;
    
    @Column(name = "total_hours")
    private BigDecimal totalHours = BigDecimal.ZERO;
    
    @Column(name = "target_date")
    private LocalDate targetDate;
    
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
    }
    
    public enum LearningItemStatus {
        NOT_STARTED,
        IN_PROGRESS,
        COMPLETED,
        ON_HOLD
    }
    
    public enum Priority {
        LOW,
        MEDIUM,
        HIGH
    }
}
