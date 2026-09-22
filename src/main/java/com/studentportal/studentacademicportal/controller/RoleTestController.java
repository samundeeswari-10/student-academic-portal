package com.studentportal.studentacademicportal.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class RoleTestController {

    @GetMapping("/api/student-area")
    @PreAuthorize("hasRole('STUDENT')")
    public String studentArea() {
        return "Welcome to the student area";
    }

    @GetMapping("/api/faculty-area")
    @PreAuthorize("hasRole('FACULTY')")
    public String facultyArea() {
        return "Welcome to the faculty area";
    }

    @GetMapping("/api/admin-area")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminArea() {
        return "Welcome to the admin area";
    }
}