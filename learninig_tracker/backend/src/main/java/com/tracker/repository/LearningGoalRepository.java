package com.tracker.repository;

import com.tracker.model.LearningGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface LearningGoalRepository extends JpaRepository<LearningGoal, UUID> {
    
    List<LearningGoal> findByProjectId(UUID projectId);
    
    List<LearningGoal> findByCompletedTrue();
    
    List<LearningGoal> findByCompletedFalse();
    
    List<LearningGoal> findByDueDateBefore(LocalDate date);
    
    @Query("SELECT g FROM LearningGoal g WHERE g.completed = false AND g.dueDate <= :date ORDER BY g.dueDate ASC")
    List<LearningGoal> findUpcomingGoals(LocalDate date);
    
    @Query("SELECT g FROM LearningGoal g WHERE g.completed = false AND g.dueDate <= :date AND g.priority = 'HIGH' ORDER BY g.dueDate ASC")
    List<LearningGoal> findHighPriorityUpcomingGoals(LocalDate date);
}
