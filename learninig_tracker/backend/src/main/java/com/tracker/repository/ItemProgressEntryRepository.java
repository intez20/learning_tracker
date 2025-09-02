package com.tracker.repository;

import com.tracker.model.ItemProgressEntry;
import com.tracker.model.LearningItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ItemProgressEntryRepository extends JpaRepository<ItemProgressEntry, UUID> {
    
    List<ItemProgressEntry> findByLearningItem(LearningItem learningItem);
    
    List<ItemProgressEntry> findByLearningItemOrderByDateDesc(LearningItem learningItem);
    
    List<ItemProgressEntry> findByDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT SUM(pe.hoursSpent) FROM ItemProgressEntry pe WHERE pe.learningItem = :learningItem")
    Double sumHoursSpentByLearningItem(LearningItem learningItem);
}
