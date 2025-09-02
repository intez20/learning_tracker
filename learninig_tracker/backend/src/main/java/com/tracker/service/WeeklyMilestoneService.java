package com.tracker.service;

import com.tracker.model.WeeklyMilestone;
import com.tracker.repository.WeeklyMilestoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WeeklyMilestoneService {

    private final WeeklyMilestoneRepository weeklyMilestoneRepository;

    public List<WeeklyMilestone> getAllWeeklyMilestones() {
        return weeklyMilestoneRepository.findAll();
    }

    public Optional<WeeklyMilestone> getWeeklyMilestoneById(UUID id) {
        return weeklyMilestoneRepository.findById(id);
    }

    public WeeklyMilestone createWeeklyMilestone(WeeklyMilestone weeklyMilestone) {
        return weeklyMilestoneRepository.save(weeklyMilestone);
    }

    public Optional<WeeklyMilestone> updateWeeklyMilestone(UUID id, WeeklyMilestone milestoneDetails) {
        return weeklyMilestoneRepository.findById(id)
                .map(existingMilestone -> {
                    existingMilestone.setTitle(milestoneDetails.getTitle());
                    existingMilestone.setDescription(milestoneDetails.getDescription());
                    existingMilestone.setProjectId(milestoneDetails.getProjectId());
                    existingMilestone.setWeekStartDate(milestoneDetails.getWeekStartDate());
                    existingMilestone.setWeekEndDate(milestoneDetails.getWeekEndDate());
                    existingMilestone.setCompleted(milestoneDetails.getCompleted());
                    existingMilestone.setCompletedDate(milestoneDetails.getCompletedDate());
                    existingMilestone.setPlannedTasks(milestoneDetails.getPlannedTasks());
                    existingMilestone.setCompletedTasks(milestoneDetails.getCompletedTasks());
                    existingMilestone.setReflection(milestoneDetails.getReflection());
                    return weeklyMilestoneRepository.save(existingMilestone);
                });
    }

    public boolean deleteWeeklyMilestone(UUID id) {
        return weeklyMilestoneRepository.findById(id)
                .map(milestone -> {
                    weeklyMilestoneRepository.delete(milestone);
                    return true;
                })
                .orElse(false);
    }

    public Optional<WeeklyMilestone> markMilestoneAsCompleted(UUID id) {
        return weeklyMilestoneRepository.findById(id)
                .map(milestone -> {
                    milestone.setCompleted(true);
                    milestone.setCompletedDate(LocalDate.now());
                    return weeklyMilestoneRepository.save(milestone);
                });
    }

    public List<WeeklyMilestone> getWeeklyMilestonesByProject(UUID projectId) {
        return weeklyMilestoneRepository.findByProjectId(projectId);
    }

    public List<WeeklyMilestone> getWeeklyMilestonesInDateRange(LocalDate startDate, LocalDate endDate) {
        return weeklyMilestoneRepository.findByWeekStartDateBetween(startDate, endDate);
    }

    public List<WeeklyMilestone> getCompletedMilestones() {
        return weeklyMilestoneRepository.findByCompletedTrue();
    }

    public List<WeeklyMilestone> getIncompleteMilestones() {
        return weeklyMilestoneRepository.findByCompletedFalse();
    }

    public List<WeeklyMilestone> getCurrentAndOverdueMilestones() {
        return weeklyMilestoneRepository.findCurrentAndOverdueMilestones(LocalDate.now());
    }

    public List<WeeklyMilestone> getUpcomingMilestones() {
        return weeklyMilestoneRepository.findUpcomingMilestones(LocalDate.now());
    }
}
