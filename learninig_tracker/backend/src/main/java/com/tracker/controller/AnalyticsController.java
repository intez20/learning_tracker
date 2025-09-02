package com.tracker.controller;

import com.tracker.dto.analytics.*;
import com.tracker.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/overview")
    public ResponseEntity<AnalyticsOverviewDTO> getOverview() {
        AnalyticsOverviewDTO overview = analyticsService.getOverview();
        return ResponseEntity.ok(overview);
    }

    @GetMapping("/progress")
    public ResponseEntity<AnalyticsProgressDTO> getProgressData(@RequestParam(value = "timeRange", defaultValue = "30d") String timeRange) {
        AnalyticsProgressDTO progressData = analyticsService.getProgressData(timeRange);
        return ResponseEntity.ok(progressData);
    }

    @GetMapping("/recent-activity")
    public ResponseEntity<List<ActivityDTO>> getRecentActivity(@RequestParam(value = "limit", defaultValue = "4") int limit) {
        List<ActivityDTO> activities = analyticsService.getRecentActivity(limit);
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/insights")
    public ResponseEntity<AnalyticsInsightsDTO> getInsights() {
        AnalyticsInsightsDTO insights = analyticsService.getInsights();
        return ResponseEntity.ok(insights);
    }
}
