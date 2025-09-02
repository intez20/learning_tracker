package com.tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemProgressEntryDTO {
    private UUID id;
    private UUID learningItemId;
    private String learningItemTitle;
    private LocalDate date;
    private BigDecimal hoursSpent;
    private Integer progress;
    private String notes;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
