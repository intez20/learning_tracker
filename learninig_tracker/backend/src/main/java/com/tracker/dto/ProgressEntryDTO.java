package com.tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgressEntryDTO {
    private UUID id;
    private LocalDate date;
    private int minutesSpent;
    private String description;
    private UUID projectId;
    private String projectName;
    private String challenges;
    private String learnings;
    private String nextSteps;
    private String mood;
}
