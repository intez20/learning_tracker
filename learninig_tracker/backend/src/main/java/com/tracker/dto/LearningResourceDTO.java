package com.tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LearningResourceDTO {
    private UUID id;
    private String title;
    private String url;
    private String description;
    private String resourceType;
    private String author;
    private UUID projectId;
    private String projectName;
    private Boolean isPrimary;
    private String notes;
}
