package com.studentportal.studentacademicportal.controller;

import com.studentportal.studentacademicportal.dto.AssignmentResponse;
import com.studentportal.studentacademicportal.entity.Assignment;
import com.studentportal.studentacademicportal.service.AssignmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY')")
    public ResponseEntity<AssignmentResponse> createAssignment(
            @RequestParam Long subjectId,
            @RequestBody Assignment assignment) {

        Assignment saved =
                assignmentService.createAssignment(
                        subjectId,
                        assignment);

        return ResponseEntity.ok(toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<AssignmentResponse>> getAllAssignments() {

        List<AssignmentResponse> assignments =
                assignmentService.getAllAssignments()
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<AssignmentResponse>>
    getAssignmentsBySubject(
            @PathVariable Long subjectId) {

        List<AssignmentResponse> assignments =
                assignmentService
                        .getAssignmentsBySubject(subjectId)
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssignmentResponse> getAssignment(
            @PathVariable Long id) {

        return assignmentService.getAssignmentById(id)
                .map(assignment ->
                        ResponseEntity.ok(toResponse(assignment)))
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY')")
    public ResponseEntity<AssignmentResponse> updateAssignment(
            @PathVariable Long id,
            @RequestBody Assignment assignment) {

        Assignment updated =
                assignmentService.updateAssignment(
                        id,
                        assignment);

        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAssignment(
            @PathVariable Long id) {

        boolean deleted =
                assignmentService.deleteAssignment(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    private AssignmentResponse toResponse(
            Assignment assignment) {

        return new AssignmentResponse(
                assignment.getId(),
                assignment.getTitle(),
                assignment.getDescription(),
                assignment.getDueDate(),
                assignment.getSubject().getId(),
                assignment.getSubject().getName(),
                assignment.getSubject().getCode()
        );
    }
}