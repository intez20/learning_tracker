package com.tracker.service;

import com.tracker.model.LearningGoal;
import com.tracker.repository.LearningGoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LearningGoalService {

    private final LearningGoalRepository learningGoalRepository;

    public List<LearningGoal> getAllLearningGoals() {
        return learningGoalRepository.findAll();
    }

    public Optional<LearningGoal> getLearningGoalById(UUID id) {
        return learningGoalRepository.findById(id);
    }

    public LearningGoal createLearningGoal(LearningGoal learningGoal) {
        return learningGoalRepository.save(learningGoal);
    }

    public Optional<LearningGoal> updateLearningGoal(UUID id, LearningGoal goalDetails) {
        return learningGoalRepository.findById(id)
                .map(existingGoal -> {
                    existingGoal.setTitle(goalDetails.getTitle());
                    existingGoal.setDescription(goalDetails.getDescription());
                    existingGoal.setProjectId(goalDetails.getProjectId());
                    existingGoal.setDueDate(goalDetails.getDueDate());
                    existingGoal.setCompleted(goalDetails.getCompleted());
                    existingGoal.setCompletedDate(goalDetails.getCompletedDate());
                    existingGoal.setPriority(goalDetails.getPriority());
                    return learningGoalRepository.save(existingGoal);
                });
    }

    public boolean deleteLearningGoal(UUID id) {
        return learningGoalRepository.findById(id)
                .map(goal -> {
                    learningGoalRepository.delete(goal);
                    return true;
                })
                .orElse(false);
    }

    public Optional<LearningGoal> markGoalAsCompleted(UUID id) {
        return learningGoalRepository.findById(id)
                .map(goal -> {
                    goal.setCompleted(true);
                    goal.setCompletedDate(LocalDate.now());
                    return learningGoalRepository.save(goal);
                });
    }

    public List<LearningGoal> getLearningGoalsByProject(UUID projectId) {
        return learningGoalRepository.findByProjectId(projectId);
    }

    public List<LearningGoal> getCompletedGoals() {
        return learningGoalRepository.findByCompletedTrue();
    }

    public List<LearningGoal> getIncompleteGoals() {
        return learningGoalRepository.findByCompletedFalse();
    }

    public List<LearningGoal> getOverdueGoals() {
        return learningGoalRepository.findByDueDateBefore(LocalDate.now());
    }

    public List<LearningGoal> getUpcomingGoals(LocalDate date) {
        return learningGoalRepository.findUpcomingGoals(date);
    }

    public List<LearningGoal> getHighPriorityUpcomingGoals(LocalDate date) {
        return learningGoalRepository.findHighPriorityUpcomingGoals(date);
    }
}
