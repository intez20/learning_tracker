package com.tracker.repository;

import com.tracker.model.LearningResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LearningResourceRepository extends JpaRepository<LearningResource, UUID> {
    
    List<LearningResource> findByProjectId(UUID projectId);
    
    List<LearningResource> findByIsPrimaryTrue();
    
    List<LearningResource> findByResourceType(String resourceType);
}
