package com.tracker.service.impl;

import com.tracker.dto.GoalDTO;
import com.tracker.mapper.EntityMapper;
import com.tracker.model.Goal;
import com.tracker.model.LearningItem;
import com.tracker.repository.GoalRepository;
import com.tracker.repository.LearningItemRepository;
import com.tracker.service.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {
    
    private final GoalRepository goalRepository;
    private final LearningItemRepository learningItemRepository;
    private final EntityMapper entityMapper;
    
    @Override
    public List<GoalDTO> getAllGoals() {
        return goalRepository.findAll().stream()
                .map(entityMapper::mapToGoalDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<GoalDTO> getGoalById(UUID id) {
        return goalRepository.findById(id)
                .map(entityMapper::mapToGoalDTO);
    }
    
    @Override
    public List<GoalDTO> getGoalsByLearningItem(UUID learningItemId) {
        return learningItemRepository.findById(learningItemId)
                .map(learningItem -> goalRepository.findByLearningItem(learningItem).stream()
                        .map(entityMapper::mapToGoalDTO)
                        .collect(Collectors.toList()))
                .orElseGet(List::of);
    }
    
    @Override
    public List<GoalDTO> getGoalsByLearningItemAndCompleted(UUID learningItemId, Boolean completed) {
        return learningItemRepository.findById(learningItemId)
                .map(learningItem -> goalRepository.findByLearningItemAndCompleted(learningItem, completed).stream()
                        .map(entityMapper::mapToGoalDTO)
                        .collect(Collectors.toList()))
                .orElseGet(List::of);
    }
    
    @Override
    public List<GoalDTO> getOverdueGoals() {
        return goalRepository.findOverdueGoals(LocalDate.now()).stream()
                .map(entityMapper::mapToGoalDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public GoalDTO createGoal(GoalDTO goalDTO) {
        Goal goal = new Goal();
        updateGoalFromDTO(goal, goalDTO);
        goal = goalRepository.save(goal);
        return entityMapper.mapToGoalDTO(goal);
    }
    
    @Override
    @Transactional
    public Optional<GoalDTO> updateGoal(UUID id, GoalDTO goalDTO) {
        return goalRepository.findById(id)
                .map(existingGoal -> {
                    updateGoalFromDTO(existingGoal, goalDTO);
                    Goal updatedGoal = goalRepository.save(existingGoal);
                    return entityMapper.mapToGoalDTO(updatedGoal);
                });
    }
    
    @Override
    @Transactional
    public Optional<GoalDTO> completeGoal(UUID id) {
        return goalRepository.findById(id)
                .map(goal -> {
                    goal.setCompleted(true);
                    goal.setCompletedDate(ZonedDateTime.now());
                    Goal updatedGoal = goalRepository.save(goal);
                    return entityMapper.mapToGoalDTO(updatedGoal);
                });
    }
    
    @Override
    @Transactional
    public boolean deleteGoal(UUID id) {
        return goalRepository.findById(id)
                .map(goal -> {
                    goalRepository.delete(goal);
                    return true;
                })
                .orElse(false);
    }
    
    private void updateGoalFromDTO(Goal goal, GoalDTO dto) {
        goal.setTitle(dto.getTitle());
        goal.setDescription(dto.getDescription());
        goal.setDueDate(dto.getDueDate());
        goal.setCompleted(dto.getCompleted() != null ? dto.getCompleted() : false);
        goal.setCompletedDate(dto.getCompletedDate());
        goal.setPriority(dto.getPriority());
        
        if (dto.getLearningItemId() != null) {
            learningItemRepository.findById(dto.getLearningItemId())
                    .ifPresent(goal::setLearningItem);
        }
    }
}
