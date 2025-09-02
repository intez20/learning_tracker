package com.tracker.repository;

import com.tracker.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    
    List<Project> findByStatus(String status);
    
    List<Project> findByCategoryId(UUID categoryId);
    
    List<Project> findByStatusNot(String status);
    
    List<Project> findByPriority(String priority);
    
    @Query("SELECT p FROM Project p WHERE p.progressPercentage < 100 AND p.status = 'active' ORDER BY p.targetCompletionDate ASC NULLS LAST")
    List<Project> findActiveProjectsOrderByTargetDate();
}
