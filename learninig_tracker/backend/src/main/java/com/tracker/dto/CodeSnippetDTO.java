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
public class CodeSnippetDTO {
    private UUID id;
    private String title;
    private String description;
    private String code;
    private String language;
    private String tags;
    private UUID projectId;
    private String projectName;
}
