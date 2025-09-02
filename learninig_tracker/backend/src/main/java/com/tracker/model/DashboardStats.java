package com.tracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import java.math.BigDecimal;

@Entity
@Immutable
@Table(name = "dashboard_stats")
@Subselect("SELECT 1 as id, * FROM dashboard_stats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStats {
    
    @Id
    private Long id;
    
    private Long activeProjects;
    
    private Long completedProjects;
    
    private BigDecimal totalHoursAllProjects;
    
    private Long sessionsThisWeek;
    
    private Double avgConfidence30days;
    
    private Long studyDays30days;
}
