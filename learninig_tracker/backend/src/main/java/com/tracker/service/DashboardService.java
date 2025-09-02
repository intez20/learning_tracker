package com.tracker.service;

import com.tracker.model.DashboardStats;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardService {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    private final ProgressEntryService progressEntryService;
    private final LearningGoalService learningGoalService;
    private final ProjectService projectService;
    
    public DashboardStats getDashboardStats() {
        // This would typically be a custom query to fetch the view
        // For example:
        return entityManager.createQuery(
                "SELECT d FROM DashboardStats d", DashboardStats.class)
                .getSingleResult();
    }
    
    public Long getTotalLearningTimeLastWeek() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(7);
        return progressEntryService.getTotalTimeSpentInDateRange(startDate, endDate);
    }
    
    public Long getTotalLearningTimeLastMonth() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(1);
        return progressEntryService.getTotalTimeSpentInDateRange(startDate, endDate);
    }
    
    public List<LocalDate> getStreakDays() {
        // Example query to get consecutive days with learning activity
        // This would typically be a custom query
        return entityManager.createQuery(
                "SELECT DISTINCT p.date FROM ProgressEntry p " +
                "WHERE p.date >= :startDate " +
                "ORDER BY p.date", LocalDate.class)
                .setParameter("startDate", LocalDate.now().minusMonths(1))
                .getResultList();
    }
    
    public List<Object[]> getTimeSpentByCategory() {
        // Example query to get time spent grouped by category
        return entityManager.createQuery(
                "SELECT c.name, SUM(p.minutesSpent) " +
                "FROM ProgressEntry p " +
                "JOIN Project pr ON p.projectId = pr.id " +
                "JOIN Category c ON pr.categoryId = c.id " +
                "GROUP BY c.name", Object[].class)
                .getResultList();
    }
    
    public List<Object[]> getTimeSpentByProject() {
        // Example query to get time spent grouped by project
        return entityManager.createQuery(
                "SELECT pr.name, SUM(p.minutesSpent) " +
                "FROM ProgressEntry p " +
                "JOIN Project pr ON p.projectId = pr.id " +
                "GROUP BY pr.name", Object[].class)
                .getResultList();
    }
    
    public Long getCountOfIncompleteHighPriorityGoals() {
        return (long) learningGoalService.getHighPriorityUpcomingGoals(LocalDate.now().plusDays(7)).size();
    }
    
    public List<Object[]> getProjectCompletionPercentages() {
        // Custom logic to calculate completion percentages
        // This is a simplified example
        List<Object[]> result = entityManager.createQuery(
                "SELECT p.id, p.name, " +
                "(SELECT COUNT(g) FROM LearningGoal g WHERE g.projectId = p.id AND g.completed = true) * 1.0 / " +
                "(SELECT COUNT(g) FROM LearningGoal g WHERE g.projectId = p.id) * 100 " +
                "FROM Project p " +
                "WHERE p.status != 'COMPLETED'", Object[].class)
                .getResultList();
        
        return result;
    }
}
