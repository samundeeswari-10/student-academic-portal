package com.studentportal.studentacademicportal.controller;

import com.studentportal.studentacademicportal.dto.AssignmentSubmissionResponse;
import com.studentportal.studentacademicportal.entity.AssignmentSubmission;
import com.studentportal.studentacademicportal.service.AssignmentSubmissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
public class AssignmentSubmissionController {

    private final AssignmentSubmissionService submissionService;

    public AssignmentSubmissionController(
            AssignmentSubmissionService submissionService) {

        this.submissionService = submissionService;
    }

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<AssignmentSubmissionResponse> submitAssignment(
            @RequestParam Long studentId,
            @RequestParam Long assignmentId,
            @RequestBody AssignmentSubmission submission) {

        AssignmentSubmission saved =
                submissionService.submitAssignment(
                        studentId,
                        assignmentId,
                        submission);

        return ResponseEntity.ok(toResponse(saved));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<AssignmentSubmissionResponse>>
    getStudentSubmissions(
            @PathVariable Long studentId) {

        List<AssignmentSubmissionResponse> submissions =
                submissionService
                        .getSubmissionsByStudent(studentId)
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return ResponseEntity.ok(submissions);
    }

    @GetMapping("/assignment/{assignmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY')")
    public ResponseEntity<List<AssignmentSubmissionResponse>>
    getAssignmentSubmissions(
            @PathVariable Long assignmentId) {

        List<AssignmentSubmissionResponse> submissions =
                submissionService
                        .getSubmissionsByAssignment(assignmentId)
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return ResponseEntity.ok(submissions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssignmentSubmissionResponse>
    getSubmission(@PathVariable Long id) {

        return submissionService.getSubmissionById(id)
                .map(submission ->
                        ResponseEntity.ok(toResponse(submission)))
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<AssignmentSubmissionResponse>
    updateSubmission(
            @PathVariable Long id,
            @RequestBody AssignmentSubmission submission) {

        AssignmentSubmission updated =
                submissionService.updateSubmission(
                        id,
                        submission);

        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSubmission(
            @PathVariable Long id) {

        boolean deleted =
                submissionService.deleteSubmission(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    private AssignmentSubmissionResponse toResponse(
            AssignmentSubmission submission) {

        return new AssignmentSubmissionResponse(
                submission.getId(),
                submission.getStudent().getId(),
                submission.getStudent().getName(),
                submission.getAssignment().getId(),
                submission.getAssignment().getTitle(),
                submission.getContent(),
                submission.getSubmittedDate(),
                submission.getStatus()
        );
    }
}