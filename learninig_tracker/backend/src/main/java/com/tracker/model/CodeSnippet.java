package com.tracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "code_snippets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeSnippet {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    private Project project;
    
    // Add projectId field that service expects
    @Column(name = "project_id", insertable = false, updatable = false)
    private UUID projectId;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "progress_entry_id")
    private ProgressEntry progressEntry;
    
    @Column(nullable = false)
    private String title;
    
    private String description;
    
    @Column(name = "code_content", nullable = false)
    private String codeContent;
    
    // Add code alias that service expects
    private String code;
    
    @Column(nullable = false)
    private String language;
    
    @Column(columnDefinition = "text[]")
    private String[] tags;
    
    @Column(name = "github_link")
    private String githubLink;
    
    @Column(name = "is_working")
    private Boolean isWorking = true;
    
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
