package com.tracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "learning_resources")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LearningResource {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
    
    // Add projectId field that service expects
    @Column(name = "project_id", insertable = false, updatable = false)
    private UUID projectId;
    
    @Column(nullable = false)
    private String title;
    
    // Add description field that service expects
    private String description;
    
    @Column(nullable = false)
    private String url;
    
    @Column(name = "resource_type")
    private String resourceType;
    
    private String platform;
    
    @Column(name = "instructor_author")
    private String instructorAuthor;
    
    // Add author alias that service expects
    private String author;
    
    @Column(name = "is_primary")
    private Boolean isPrimary = false;
    
    @Column(name = "completion_percentage")
    private Integer completionPercentage = 0;
    
    private Integer rating;
    
    @Column(length = 500)
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
}
