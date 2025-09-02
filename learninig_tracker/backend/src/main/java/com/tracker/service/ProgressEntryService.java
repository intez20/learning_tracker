package com.tracker.service;

import com.tracker.model.ProgressEntry;
import com.tracker.repository.ProgressEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProgressEntryService {

    private final ProgressEntryRepository progressEntryRepository;

    public List<ProgressEntry> getAllProgressEntries() {
        return progressEntryRepository.findAll();
    }

    public Optional<ProgressEntry> getProgressEntryById(UUID id) {
        return progressEntryRepository.findById(id);
    }

    public ProgressEntry createProgressEntry(ProgressEntry progressEntry) {
        return progressEntryRepository.save(progressEntry);
    }

    public Optional<ProgressEntry> updateProgressEntry(UUID id, ProgressEntry entryDetails) {
        return progressEntryRepository.findById(id)
                .map(existingEntry -> {
                    existingEntry.setDate(entryDetails.getDate());
                    existingEntry.setMinutesSpent(entryDetails.getMinutesSpent());
                    existingEntry.setDescription(entryDetails.getDescription());
                    existingEntry.setProjectId(entryDetails.getProjectId());
                    existingEntry.setChallenges(entryDetails.getChallenges());
                    existingEntry.setLearnings(entryDetails.getLearnings());
                    existingEntry.setNextSteps(entryDetails.getNextSteps());
                    existingEntry.setMood(entryDetails.getMood());
                    return progressEntryRepository.save(existingEntry);
                });
    }

    public boolean deleteProgressEntry(UUID id) {
        return progressEntryRepository.findById(id)
                .map(entry -> {
                    progressEntryRepository.delete(entry);
                    return true;
                })
                .orElse(false);
    }

    public List<ProgressEntry> getProgressEntriesByProject(UUID projectId) {
        return progressEntryRepository.findByProjectId(projectId);
    }

    public List<ProgressEntry> getProgressEntriesInDateRange(LocalDate startDate, LocalDate endDate) {
        return progressEntryRepository.findByDateBetween(startDate, endDate);
    }

    public List<ProgressEntry> getProgressEntriesInDateRangeOrderByDate(LocalDate startDate, LocalDate endDate) {
        return progressEntryRepository.findEntriesInDateRangeOrderByDate(startDate, endDate);
    }

    public Long getTotalTimeSpentOnProject(UUID projectId) {
        return progressEntryRepository.getTotalTimeSpentOnProject(projectId);
    }

    public Long getTotalTimeSpentInDateRange(LocalDate startDate, LocalDate endDate) {
        return progressEntryRepository.getTotalTimeSpentInDateRange(startDate, endDate);
    }
}
