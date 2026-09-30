package com.studentportal.studentacademicportal.controller;

import com.studentportal.studentacademicportal.entity.Faculty;
import com.studentportal.studentacademicportal.service.FacultyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Faculty createFaculty(
            @RequestBody Faculty faculty) {

        return facultyService.saveFaculty(faculty);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Faculty> getAllFaculty() {

        return facultyService.getAllFaculty();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Faculty> getFaculty(
            @PathVariable Long id) {

        return facultyService.getFacultyById(id)
                .map(ResponseEntity::ok)
                .orElseGet(
                        () -> ResponseEntity.notFound().build()
                );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Faculty> updateFaculty(
            @PathVariable Long id,
            @RequestBody Faculty faculty) {

        Faculty updated =
                facultyService.updateFaculty(id, faculty);

        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteFaculty(
            @PathVariable Long id) {

        boolean deleted =
                facultyService.deleteFaculty(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}