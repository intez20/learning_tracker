package com.tracker.repository;

import com.tracker.model.Goal;
import com.tracker.model.LearningItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface GoalRepository extends JpaRepository<Goal, UUID> {
    
    List<Goal> findByLearningItem(LearningItem learningItem);
    
    List<Goal> findByLearningItemAndCompleted(LearningItem learningItem, Boolean completed);
    
    @Query("SELECT g FROM Goal g WHERE g.dueDate <= :date AND g.completed = false")
    List<Goal> findOverdueGoals(LocalDate date);
    
    @Query("SELECT COUNT(g) FROM Goal g WHERE g.learningItem = :learningItem")
    Integer countByLearningItem(LearningItem learningItem);
    
    @Query("SELECT COUNT(g) FROM Goal g WHERE g.learningItem = :learningItem AND g.completed = true")
    Integer countCompletedByLearningItem(LearningItem learningItem);
}
