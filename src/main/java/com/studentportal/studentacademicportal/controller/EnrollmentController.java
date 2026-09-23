package com.studentportal.studentacademicportal.controller;

import com.studentportal.studentacademicportal.dto.EnrollmentResponse;
import com.studentportal.studentacademicportal.entity.Enrollment;
import com.studentportal.studentacademicportal.service.EnrollmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EnrollmentResponse> enrollStudent(
            @RequestParam Long studentId,
            @RequestParam Long subjectId) {

        Enrollment enrollment =
                enrollmentService.enrollStudent(
                        studentId,
                        subjectId
                );

        return ResponseEntity.ok(toResponse(enrollment));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentResponse>>
    getStudentEnrollments(
            @PathVariable Long studentId) {

        List<EnrollmentResponse> enrollments =
                enrollmentService
                        .getEnrollmentsByStudent(studentId)
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/subject/{subjectId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY')")
    public ResponseEntity<List<EnrollmentResponse>>
    getSubjectEnrollments(
            @PathVariable Long subjectId) {

        List<EnrollmentResponse> enrollments =
                enrollmentService
                        .getEnrollmentsBySubject(subjectId)
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return ResponseEntity.ok(enrollments);
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteEnrollment(
            @PathVariable Long id) {

        boolean deleted =
                enrollmentService.deleteEnrollment(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    private EnrollmentResponse toResponse(
            Enrollment enrollment) {

        return new EnrollmentResponse(
                enrollment.getId(),

                enrollment.getStudent().getId(),
                enrollment.getStudent().getName(),
                enrollment.getStudent().getEmail(),

                enrollment.getSubject().getId(),
                enrollment.getSubject().getName(),
                enrollment.getSubject().getCode()
        );
    }
}