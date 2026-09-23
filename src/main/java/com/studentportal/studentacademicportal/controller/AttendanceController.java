package com.studentportal.studentacademicportal.controller;

import com.studentportal.studentacademicportal.dto.AttendanceResponse;
import com.studentportal.studentacademicportal.entity.Attendance;
import com.studentportal.studentacademicportal.service.AttendanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY')")
    public ResponseEntity<AttendanceResponse> createAttendance(
            @RequestParam Long studentId,
            @RequestParam Long subjectId,
            @RequestBody Attendance attendance) {

        Attendance saved =
                attendanceService.createAttendance(
                        studentId,
                        subjectId,
                        attendance);

        return ResponseEntity.ok(toResponse(saved));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<AttendanceResponse>> getStudentAttendance(
            @PathVariable Long studentId) {

        List<AttendanceResponse> attendance =
                attendanceService
                        .getAttendanceByStudent(studentId)
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/subject/{subjectId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY')")
    public ResponseEntity<List<AttendanceResponse>> getSubjectAttendance(
            @PathVariable Long subjectId) {

        List<AttendanceResponse> attendance =
                attendanceService
                        .getAttendanceBySubject(subjectId)
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/student/{studentId}/subject/{subjectId}/percentage")
    public ResponseEntity<Double> getAttendancePercentage(
            @PathVariable Long studentId,
            @PathVariable Long subjectId) {

        double percentage =
                attendanceService.getAttendancePercentage(
                        studentId,
                        subjectId);

        return ResponseEntity.ok(percentage);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY')")
    public ResponseEntity<AttendanceResponse> updateAttendance(
            @PathVariable Long id,
            @RequestBody Attendance attendance) {

        Attendance updated =
                attendanceService.updateAttendance(id, attendance);

        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAttendance(
            @PathVariable Long id) {

        boolean deleted =
                attendanceService.deleteAttendance(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    private AttendanceResponse toResponse(Attendance attendance) {

        return new AttendanceResponse(
                attendance.getId(),
                attendance.getStudent().getId(),
                attendance.getStudent().getName(),
                attendance.getSubject().getId(),
                attendance.getSubject().getName(),
                attendance.getSubject().getCode(),
                attendance.getAttendanceDate(),
                attendance.isPresent()
        );
    }
}