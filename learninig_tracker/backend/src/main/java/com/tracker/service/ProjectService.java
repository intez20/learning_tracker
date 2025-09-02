package com.tracker.service;

import com.tracker.model.Project;
import com.tracker.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> getProjectById(UUID id) {
        return projectRepository.findById(id);
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public Optional<Project> updateProject(UUID id, Project projectDetails) {
        return projectRepository.findById(id)
                .map(existingProject -> {
                    existingProject.setName(projectDetails.getName());
                    existingProject.setDescription(projectDetails.getDescription());
                    existingProject.setStatus(projectDetails.getStatus());
                    existingProject.setPriority(projectDetails.getPriority());
                    existingProject.setCategoryId(projectDetails.getCategoryId());
                    existingProject.setEstimatedCompletionDate(projectDetails.getEstimatedCompletionDate());
                    existingProject.setStartDate(projectDetails.getStartDate());
                    existingProject.setCompletionDate(projectDetails.getCompletionDate());
                    return projectRepository.save(existingProject);
                });
    }

    public boolean deleteProject(UUID id) {
        return projectRepository.findById(id)
                .map(project -> {
                    projectRepository.delete(project);
                    return true;
                })
                .orElse(false);
    }

    public List<Project> getProjectsByCategory(UUID categoryId) {
        return projectRepository.findByCategoryId(categoryId);
    }

    public List<Project> getProjectsByStatus(String status) {
        return projectRepository.findByStatus(status);
    }

    public List<Project> getProjectsByPriority(String priority) {
        return projectRepository.findByPriority(priority);
    }

    public List<Project> getActiveProjects() {
        return projectRepository.findByStatusNot("COMPLETED");
    }
}
