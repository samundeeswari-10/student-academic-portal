package com.studentportal.studentacademicportal.controller;

import java.util.List;
import java.util.Optional;

import com.studentportal.studentacademicportal.entity.Student;
import com.studentportal.studentacademicportal.service.StudentService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@RestController
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // ADMIN can create students
    @PostMapping("/api/students")
    @PreAuthorize("hasRole('ADMIN')")
    public Student createStudent(
            @RequestBody Student student,
            Authentication authentication) {

        System.out.println("USER: " + authentication.getName());
        System.out.println("AUTHORITIES: " + authentication.getAuthorities());

        return studentService.saveStudent(student);
    }



    // Get all students
    @GetMapping("/api/students")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    // Get student by ID
    @GetMapping("/api/students/{id}")
    public ResponseEntity<Student> getStudentById(
            @PathVariable Long id) {

        Optional<Student> student =
                studentService.getStudentById(id);

        if (student.isPresent()) {
            return ResponseEntity.ok(student.get());
        }

        return ResponseEntity.notFound().build();
    }

    // ADMIN can delete students
    @DeleteMapping("/api/students/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteStudent(
            @PathVariable Long id) {

        studentService.deleteStudent(id);

        return ResponseEntity.noContent().build();
    }

    // ADMIN can update students
    @PutMapping("/api/students/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Student> updateStudent(
            @PathVariable Long id,
            @RequestBody Student student) {

        Student updatedStudent =
                studentService.updateStudent(id, student);

        if (updatedStudent != null) {
            return ResponseEntity.ok(updatedStudent);
        }

        return ResponseEntity.notFound().build();
    }
}