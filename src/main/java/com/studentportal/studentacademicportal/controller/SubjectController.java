package com.studentportal.studentacademicportal.controller;

import com.studentportal.studentacademicportal.dto.SubjectResponse;
import com.studentportal.studentacademicportal.entity.Subject;
import com.studentportal.studentacademicportal.service.SubjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }





    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubjectResponse> createSubject(
            @RequestParam Long departmentId,
            @RequestParam Long facultyId,
            @RequestBody Subject subject) {

        Subject saved =
                subjectService.createSubject(
                        subject,
                        departmentId,
                        facultyId
                );

        return ResponseEntity.ok(toResponse(saved));
    }

//    @GetMapping
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<List<SubjectResponse>> getAllSubjects() {
//
//        List<SubjectResponse> subjects =
//                subjectService.getAllSubjects()
//                        .stream()
//                        .map(this::toResponse)
//                        .toList();
//
//        return ResponseEntity.ok(subjects);
//    }
@GetMapping
public ResponseEntity<List<SubjectResponse>> getAllSubjects() {

    List<SubjectResponse> subjects =
            subjectService.getAllSubjects()
                    .stream()
                    .map(this::toResponse)
                    .toList();

    return ResponseEntity.ok(subjects);
}

    @GetMapping("/{id}")
    public ResponseEntity<SubjectResponse> getSubject(
            @PathVariable Long id) {

        return subjectService.getSubjectById(id)
                .map(subject ->
                        ResponseEntity.ok(toResponse(subject)))
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<SubjectResponse>>
    getSubjectsByDepartment(
            @PathVariable Long departmentId) {

        List<SubjectResponse> subjects =
                subjectService
                        .getSubjectsByDepartment(departmentId)
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return ResponseEntity.ok(subjects);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubjectResponse> updateSubject(
            @PathVariable Long id,
            @RequestParam Long departmentId,
            @RequestParam Long facultyId,
            @RequestBody Subject subject) {

        Subject updated =
                subjectService.updateSubject(
                        id,
                        subject,
                        departmentId,
                        facultyId
                );

        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSubject(
            @PathVariable Long id) {

        boolean deleted =
                subjectService.deleteSubject(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    private SubjectResponse toResponse(Subject subject) {

        Long facultyId = null;
        String facultyName = null;
        String facultyEmail = null;

        if (subject.getFaculty() != null) {
            facultyId = subject.getFaculty().getId();
            facultyName = subject.getFaculty().getName();
            facultyEmail = subject.getFaculty().getEmail();
        }

        return new SubjectResponse(
                subject.getId(),
                subject.getName(),
                subject.getCode(),
                subject.getCredits(),

                subject.getDepartment().getId(),
                subject.getDepartment().getName(),
                subject.getDepartment().getCode(),

                facultyId,
                facultyName,
                facultyEmail
        );
    }
}