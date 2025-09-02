package com.tracker.service.impl;

import com.tracker.dto.ItemProgressEntryDTO;
import com.tracker.mapper.EntityMapper;
import com.tracker.model.ItemProgressEntry;
import com.tracker.model.LearningItem;
import com.tracker.repository.ItemProgressEntryRepository;
import com.tracker.repository.LearningItemRepository;
import com.tracker.service.ItemProgressEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemProgressEntryServiceImpl implements ItemProgressEntryService {
    
    private final ItemProgressEntryRepository progressEntryRepository;
    private final LearningItemRepository learningItemRepository;
    private final EntityMapper entityMapper;
    
    @Override
    public List<ItemProgressEntryDTO> getAllProgressEntries() {
        return progressEntryRepository.findAll().stream()
                .map(entityMapper::mapToItemProgressEntryDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<ItemProgressEntryDTO> getProgressEntryById(UUID id) {
        return progressEntryRepository.findById(id)
                .map(entityMapper::mapToItemProgressEntryDTO);
    }
    
    @Override
    public List<ItemProgressEntryDTO> getProgressEntriesByLearningItem(UUID learningItemId) {
        return learningItemRepository.findById(learningItemId)
                .map(learningItem -> progressEntryRepository.findByLearningItemOrderByDateDesc(learningItem).stream()
                        .map(entityMapper::mapToItemProgressEntryDTO)
                        .collect(Collectors.toList()))
                .orElseGet(List::of);
    }
    
    @Override
    public List<ItemProgressEntryDTO> getProgressEntriesByDateRange(LocalDate startDate, LocalDate endDate) {
        return progressEntryRepository.findByDateBetween(startDate, endDate).stream()
                .map(entityMapper::mapToItemProgressEntryDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public ItemProgressEntryDTO createProgressEntry(ItemProgressEntryDTO entryDTO) {
        ItemProgressEntry entry = new ItemProgressEntry();
        updateEntryFromDTO(entry, entryDTO);
        entry = progressEntryRepository.save(entry);
        return entityMapper.mapToItemProgressEntryDTO(entry);
    }
    
    @Override
    @Transactional
    public Optional<ItemProgressEntryDTO> updateProgressEntry(UUID id, ItemProgressEntryDTO entryDTO) {
        return progressEntryRepository.findById(id)
                .map(existingEntry -> {
                    updateEntryFromDTO(existingEntry, entryDTO);
                    ItemProgressEntry updatedEntry = progressEntryRepository.save(existingEntry);
                    return entityMapper.mapToItemProgressEntryDTO(updatedEntry);
                });
    }
    
    @Override
    @Transactional
    public boolean deleteProgressEntry(UUID id) {
        return progressEntryRepository.findById(id)
                .map(entry -> {
                    progressEntryRepository.delete(entry);
                    return true;
                })
                .orElse(false);
    }
    
    private void updateEntryFromDTO(ItemProgressEntry entry, ItemProgressEntryDTO dto) {
        entry.setDate(dto.getDate());
        entry.setHoursSpent(dto.getHoursSpent());
        entry.setProgress(dto.getProgress());
        entry.setNotes(dto.getNotes());
        
        if (dto.getLearningItemId() != null) {
            learningItemRepository.findById(dto.getLearningItemId())
                    .ifPresent(entry::setLearningItem);
        }
    }
}
