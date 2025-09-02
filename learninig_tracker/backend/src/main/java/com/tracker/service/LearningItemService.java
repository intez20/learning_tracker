package com.tracker.service;

import com.tracker.dto.LearningItemDTO;
import com.tracker.model.Category;
import com.tracker.model.LearningItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface LearningItemService {
    
    List<LearningItemDTO> getAllLearningItems();
    
    Optional<LearningItemDTO> getLearningItemById(UUID id);
    
    List<LearningItemDTO> getLearningItemsByCategory(UUID categoryId);
    
    List<LearningItemDTO> getLearningItemsByStatus(LearningItem.LearningItemStatus status);
    
    List<LearningItemDTO> searchLearningItems(String searchTerm);
    
    LearningItemDTO createLearningItem(LearningItemDTO learningItemDTO);
    
    Optional<LearningItemDTO> updateLearningItem(UUID id, LearningItemDTO learningItemDTO);
    
    boolean deleteLearningItem(UUID id);
}
