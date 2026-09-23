package com.studentportal.studentacademicportal.controller;

import com.studentportal.studentacademicportal.dto.MarkResponse;
import com.studentportal.studentacademicportal.entity.Mark;
import com.studentportal.studentacademicportal.service.MarkService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marks")
public class MarkController {

    private final MarkService markService;

    public MarkController(MarkService markService) {
        this.markService = markService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY')")
    public ResponseEntity<MarkResponse> createMark(
            @RequestParam Long studentId,
            @RequestParam Long subjectId,
            @RequestBody Mark mark) {

        Mark saved =
                markService.createMark(
                        studentId,
                        subjectId,
                        mark
                );

        return ResponseEntity.ok(toResponse(saved));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<MarkResponse>> getStudentMarks(
            @PathVariable Long studentId) {

        List<MarkResponse> marks =
                markService
                        .getMarksByStudent(studentId)
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return ResponseEntity.ok(marks);
    }

    @GetMapping("/subject/{subjectId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY')")
    public ResponseEntity<List<MarkResponse>> getSubjectMarks(
            @PathVariable Long subjectId) {

        List<MarkResponse> marks =
                markService
                        .getMarksBySubject(subjectId)
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return ResponseEntity.ok(marks);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY')")
    public ResponseEntity<MarkResponse> updateMark(
            @PathVariable Long id,
            @RequestBody Mark mark) {

        Mark updated =
                markService.updateMark(id, mark);

        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMark(
            @PathVariable Long id) {

        boolean deleted =
                markService.deleteMark(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    private MarkResponse toResponse(Mark mark) {

        return new MarkResponse(
                mark.getId(),

                mark.getStudent().getId(),
                mark.getStudent().getName(),

                mark.getSubject().getId(),
                mark.getSubject().getName(),
                mark.getSubject().getCode(),

                mark.getInternalMarks(),
                mark.getAssignmentMarks(),
                mark.getExamMarks()
        );
    }
}