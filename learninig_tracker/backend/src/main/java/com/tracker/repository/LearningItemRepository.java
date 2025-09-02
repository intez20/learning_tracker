package com.tracker.repository;

import com.tracker.model.Category;
import com.tracker.model.LearningItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LearningItemRepository extends JpaRepository<LearningItem, UUID> {
    
    List<LearningItem> findByCategory(Category category);
    
    List<LearningItem> findByStatus(LearningItem.LearningItemStatus status);
    
    @Query("SELECT li FROM LearningItem li WHERE LOWER(li.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(li.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<LearningItem> searchByTitleOrDescription(String searchTerm);
}
