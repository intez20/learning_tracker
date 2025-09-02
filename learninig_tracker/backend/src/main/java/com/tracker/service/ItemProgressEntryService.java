package com.tracker.service;

import com.tracker.dto.ItemProgressEntryDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface ItemProgressEntryService {
    
    List<ItemProgressEntryDTO> getAllProgressEntries();
    
    Optional<ItemProgressEntryDTO> getProgressEntryById(UUID id);
    
    List<ItemProgressEntryDTO> getProgressEntriesByLearningItem(UUID learningItemId);
    
    List<ItemProgressEntryDTO> getProgressEntriesByDateRange(LocalDate startDate, LocalDate endDate);
    
    ItemProgressEntryDTO createProgressEntry(ItemProgressEntryDTO progressEntryDTO);
    
    Optional<ItemProgressEntryDTO> updateProgressEntry(UUID id, ItemProgressEntryDTO progressEntryDTO);
    
    boolean deleteProgressEntry(UUID id);
}
