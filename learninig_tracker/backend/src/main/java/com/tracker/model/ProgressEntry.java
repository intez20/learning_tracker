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
@Table(name = "progress_entries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressEntry {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
    
    // Add projectId field that service expects
    @Column(name = "project_id", insertable = false, updatable = false)
    private UUID projectId;
    
    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;
    
    // Add date alias that service expects
    @Column(name = "date")
    private LocalDate date;
    
    @Column(name = "day_of_week", nullable = false)
    private String dayOfWeek;
    
    @Column(name = "focus_area", nullable = false)
    private String focusArea;
    
    @Column(name = "what_studied", nullable = false)
    private String whatStudied;
    
    @Column(name = "hours_spent", precision = 4, scale = 2, nullable = false)
    private BigDecimal hoursSpent;
    
    // Add minutesSpent field that service expects
    @Column(name = "minutes_spent")
    private Integer minutesSpent;
    
    @Column(name = "confidence_level", nullable = false)
    private Integer confidenceLevel;
    
    @Column(name = "session_notes", length = 500)
    private String sessionNotes;
    
    // Add description alias that service expects
    private String description;
    
    @Column(name = "next_steps", length = 500)
    private String nextSteps;
    
    @Column(name = "github_commit_url")
    private String githubCommitUrl;
    
    // Add fields that service expects
    private String challenges;
    private String learnings;
    private String mood;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "resource_id")
    private LearningResource resource;
    
    @Column(name = "created_at")
    private ZonedDateTime createdAt;
    
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = ZonedDateTime.now();
        updatedAt = ZonedDateTime.now();
        
        // Set day of week based on entry date
        if (entryDate != null) {
            dayOfWeek = entryDate.getDayOfWeek().toString();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = ZonedDateTime.now();
        
        // Update day of week if entry date changes
        if (entryDate != null) {
            dayOfWeek = entryDate.getDayOfWeek().toString();
        }
    }
}
