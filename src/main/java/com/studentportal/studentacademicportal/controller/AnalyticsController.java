
package com.studentportal.studentacademicportal.controller;

import com.studentportal.studentacademicportal.dto.StudentAnalyticsResponse;
import com.studentportal.studentacademicportal.dto.TargetTrackingResponse;
import com.studentportal.studentacademicportal.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<StudentAnalyticsResponse> getStudentAnalytics(
            @PathVariable Long studentId) {

        return ResponseEntity.ok(
                analyticsService.getStudentAnalytics(studentId)
        );
    }

    @GetMapping("/student/{studentId}/target")
    public ResponseEntity<TargetTrackingResponse> getTargetTracking(
            @PathVariable Long studentId,
            @RequestParam double targetPercentage) {

        return ResponseEntity.ok(
                analyticsService.getTargetTracking(
                        studentId,
                        targetPercentage
                )
        );
    }
}
