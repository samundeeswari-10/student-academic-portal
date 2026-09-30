package com.studentportal.studentacademicportal.controller;

import com.studentportal.studentacademicportal.dto.StudentAnalyticsResponse;
import com.studentportal.studentacademicportal.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<StudentAnalyticsResponse> getStudentAnalytics(
            @PathVariable Long studentId,
            Authentication authentication) {

        System.out.println("AUTHENTICATED USER: " + authentication.getName());
        System.out.println("AUTHORITIES: " + authentication.getAuthorities());

        return ResponseEntity.ok(
                analyticsService.getStudentAnalytics(studentId)
        );
    }
}