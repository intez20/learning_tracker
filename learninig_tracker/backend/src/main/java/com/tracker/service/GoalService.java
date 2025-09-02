package com.tracker.service;

import com.tracker.dto.GoalDTO;
import com.tracker.model.LearningItem;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface GoalService {
    
    List<GoalDTO> getAllGoals();
    
    Optional<GoalDTO> getGoalById(UUID id);
    
    List<GoalDTO> getGoalsByLearningItem(UUID learningItemId);
    
    List<GoalDTO> getGoalsByLearningItemAndCompleted(UUID learningItemId, Boolean completed);
    
    List<GoalDTO> getOverdueGoals();
    
    GoalDTO createGoal(GoalDTO goalDTO);
    
    Optional<GoalDTO> updateGoal(UUID id, GoalDTO goalDTO);
    
    Optional<GoalDTO> completeGoal(UUID id);
    
    boolean deleteGoal(UUID id);
}
