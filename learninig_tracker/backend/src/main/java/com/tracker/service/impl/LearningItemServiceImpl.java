package com.tracker.service.impl;

import com.tracker.dto.LearningItemDTO;
import com.tracker.mapper.EntityMapper;
import com.tracker.model.Category;
import com.tracker.model.LearningItem;
import com.tracker.repository.CategoryRepository;
import com.tracker.repository.GoalRepository;
import com.tracker.repository.LearningItemRepository;
import com.tracker.service.LearningItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LearningItemServiceImpl implements LearningItemService {
    
    private final LearningItemRepository learningItemRepository;
    private final CategoryRepository categoryRepository;
    private final GoalRepository goalRepository;
    private final EntityMapper entityMapper;
    
    @Override
    public List<LearningItemDTO> getAllLearningItems() {
        return learningItemRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<LearningItemDTO> getLearningItemById(UUID id) {
        return learningItemRepository.findById(id)
                .map(this::mapToDTO);
    }
    
    @Override
    public List<LearningItemDTO> getLearningItemsByCategory(UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .map(category -> learningItemRepository.findByCategory(category).stream()
                        .map(this::mapToDTO)
                        .collect(Collectors.toList()))
                .orElseGet(List::of);
    }
    
    @Override
    public List<LearningItemDTO> getLearningItemsByStatus(LearningItem.LearningItemStatus status) {
        return learningItemRepository.findByStatus(status).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<LearningItemDTO> searchLearningItems(String searchTerm) {
        return learningItemRepository.searchByTitleOrDescription(searchTerm).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public LearningItemDTO createLearningItem(LearningItemDTO learningItemDTO) {
        LearningItem learningItem = mapToEntity(learningItemDTO);
        learningItem = learningItemRepository.save(learningItem);
        return mapToDTO(learningItem);
    }
    
    @Override
    @Transactional
    public Optional<LearningItemDTO> updateLearningItem(UUID id, LearningItemDTO learningItemDTO) {
        return learningItemRepository.findById(id)
                .map(existingItem -> {
                    // Update fields from DTO
                    existingItem.setTitle(learningItemDTO.getTitle());
                    existingItem.setDescription(learningItemDTO.getDescription());
                    existingItem.setStatus(learningItemDTO.getStatus());
                    existingItem.setPriority(learningItemDTO.getPriority());
                    existingItem.setProgressPercentage(learningItemDTO.getProgressPercentage());
                    existingItem.setTotalHours(learningItemDTO.getTotalHours());
                    existingItem.setTargetDate(learningItemDTO.getTargetDate());
                    existingItem.setNotes(learningItemDTO.getNotes());
                    
                    // Handle category
                    if (learningItemDTO.getCategoryId() != null) {
                        categoryRepository.findById(learningItemDTO.getCategoryId())
                                .ifPresent(existingItem::setCategory);
                    } else {
                        existingItem.setCategory(null);
                    }
                    
                    LearningItem updatedItem = learningItemRepository.save(existingItem);
                    return mapToDTO(updatedItem);
                });
    }
    
    @Override
    @Transactional
    public boolean deleteLearningItem(UUID id) {
        return learningItemRepository.findById(id)
                .map(item -> {
                    learningItemRepository.delete(item);
                    return true;
                })
                .orElse(false);
    }
    
    // Helper method to map entity to DTO with additional data
    private LearningItemDTO mapToDTO(LearningItem learningItem) {
        LearningItemDTO dto = entityMapper.mapToLearningItemDTO(learningItem);
        
        // Set additional data
        if (learningItem.getId() != null) {
            dto.setGoalCount(goalRepository.countByLearningItem(learningItem));
            dto.setCompletedGoalCount(goalRepository.countCompletedByLearningItem(learningItem));
        } else {
            dto.setGoalCount(0);
            dto.setCompletedGoalCount(0);
        }
        
        // Set category info
        if (learningItem.getCategory() != null) {
            dto.setCategoryId(learningItem.getCategory().getId());
            dto.setCategoryName(learningItem.getCategory().getName());
        }
        
        return dto;
    }
    
    // Helper method to map DTO to entity
    private LearningItem mapToEntity(LearningItemDTO dto) {
        LearningItem learningItem = new LearningItem();
        learningItem.setTitle(dto.getTitle());
        learningItem.setDescription(dto.getDescription());
        learningItem.setStatus(dto.getStatus());
        learningItem.setPriority(dto.getPriority());
        learningItem.setProgressPercentage(dto.getProgressPercentage());
        learningItem.setTotalHours(dto.getTotalHours());
        learningItem.setTargetDate(dto.getTargetDate());
        learningItem.setNotes(dto.getNotes());
        
        // Set category
        if (dto.getCategoryId() != null) {
            categoryRepository.findById(dto.getCategoryId())
                    .ifPresent(learningItem::setCategory);
        }
        
        return learningItem;
    }
}
