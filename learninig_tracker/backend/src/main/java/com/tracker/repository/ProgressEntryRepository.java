package com.tracker.repository;

import com.tracker.model.ProgressEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProgressEntryRepository extends JpaRepository<ProgressEntry, UUID> {
    
    List<ProgressEntry> findByProjectId(UUID projectId);
    
    List<ProgressEntry> findByDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT p FROM ProgressEntry p WHERE p.date >= :startDate AND p.date <= :endDate ORDER BY p.date ASC")
    List<ProgressEntry> findEntriesInDateRangeOrderByDate(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT SUM(p.minutesSpent) FROM ProgressEntry p WHERE p.projectId = :projectId")
    Long getTotalTimeSpentOnProject(UUID projectId);
    
    @Query("SELECT SUM(p.minutesSpent) FROM ProgressEntry p WHERE p.date >= :startDate AND p.date <= :endDate")
    Long getTotalTimeSpentInDateRange(LocalDate startDate, LocalDate endDate);
}
