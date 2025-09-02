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
@Table(name = "projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(nullable = false)
    private String title;
    
    // Add name field that service expects
    @Column(nullable = false)
    private String name;
    
    private String description;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;
    
    // Add categoryId field that service expects
    @Column(name = "category_id", insertable = false, updatable = false)
    private UUID categoryId;
    
    @Column(name = "status")
    private String status = "active";
    
    // Add priority field that service expects
    @Column(name = "priority")
    private String priority;
    
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "target_completion_date")
    private LocalDate targetCompletionDate;
    
    // Add estimatedCompletionDate alias
    @Column(name = "estimated_completion_date")
    private LocalDate estimatedCompletionDate;
    
    @Column(name = "actual_completion_date")
    private LocalDate actualCompletionDate;
    
    // Add completionDate alias
    @Column(name = "completion_date")
    private LocalDate completionDate;
    
    @Column(name = "difficulty_level")
    private Integer difficultyLevel;
    
    @Column(name = "estimated_hours", precision = 5, scale = 2)
    private BigDecimal estimatedHours;
    
    @Column(name = "actual_hours", precision = 5, scale = 2)
    private BigDecimal actualHours = BigDecimal.ZERO;
    
    @Column(name = "progress_percentage")
    private Integer progressPercentage = 0;
    
    @Column(name = "github_url")
    private String githubUrl;
    
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
