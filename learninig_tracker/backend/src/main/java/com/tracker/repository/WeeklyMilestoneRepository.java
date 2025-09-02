package com.tracker.repository;

import com.tracker.model.WeeklyMilestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface WeeklyMilestoneRepository extends JpaRepository<WeeklyMilestone, UUID> {
    
    List<WeeklyMilestone> findByProjectId(UUID projectId);
    
    List<WeeklyMilestone> findByWeekStartDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<WeeklyMilestone> findByCompletedTrue();
    
    List<WeeklyMilestone> findByCompletedFalse();
    
    @Query("SELECT w FROM WeeklyMilestone w WHERE w.completed = false AND w.weekStartDate <= :currentDate ORDER BY w.weekStartDate ASC")
    List<WeeklyMilestone> findCurrentAndOverdueMilestones(LocalDate currentDate);
    
    @Query("SELECT w FROM WeeklyMilestone w WHERE w.weekStartDate > :currentDate ORDER BY w.weekStartDate ASC")
    List<WeeklyMilestone> findUpcomingMilestones(LocalDate currentDate);
}
