package com.tracker.service;

import com.tracker.model.LearningResource;
import com.tracker.repository.LearningResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LearningResourceService {

    private final LearningResourceRepository learningResourceRepository;

    public List<LearningResource> getAllLearningResources() {
        return learningResourceRepository.findAll();
    }

    public Optional<LearningResource> getLearningResourceById(UUID id) {
        return learningResourceRepository.findById(id);
    }

    public LearningResource createLearningResource(LearningResource learningResource) {
        return learningResourceRepository.save(learningResource);
    }

    public Optional<LearningResource> updateLearningResource(UUID id, LearningResource resourceDetails) {
        return learningResourceRepository.findById(id)
                .map(existingResource -> {
                    existingResource.setTitle(resourceDetails.getTitle());
                    existingResource.setUrl(resourceDetails.getUrl());
                    existingResource.setDescription(resourceDetails.getDescription());
                    existingResource.setResourceType(resourceDetails.getResourceType());
                    existingResource.setAuthor(resourceDetails.getAuthor());
                    existingResource.setProjectId(resourceDetails.getProjectId());
                    existingResource.setIsPrimary(resourceDetails.getIsPrimary());
                    existingResource.setNotes(resourceDetails.getNotes());
                    return learningResourceRepository.save(existingResource);
                });
    }

    public boolean deleteLearningResource(UUID id) {
        return learningResourceRepository.findById(id)
                .map(resource -> {
                    learningResourceRepository.delete(resource);
                    return true;
                })
                .orElse(false);
    }

    public List<LearningResource> getLearningResourcesByProject(UUID projectId) {
        return learningResourceRepository.findByProjectId(projectId);
    }

    public List<LearningResource> getPrimaryResources() {
        return learningResourceRepository.findByIsPrimaryTrue();
    }

    public List<LearningResource> getLearningResourcesByType(String resourceType) {
        return learningResourceRepository.findByResourceType(resourceType);
    }
}
