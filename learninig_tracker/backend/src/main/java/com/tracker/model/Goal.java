package com.tracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "goals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Goal {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(nullable = false)
    private String title;
    
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "learning_item_id", nullable = false)
    private LearningItem learningItem;
    
    @Column(name = "due_date")
    private LocalDate dueDate;
    
    private Boolean completed = false;
    
    @Column(name = "completed_date")
    private ZonedDateTime completedDate;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LearningItem.Priority priority = LearningItem.Priority.MEDIUM;
    
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
}
