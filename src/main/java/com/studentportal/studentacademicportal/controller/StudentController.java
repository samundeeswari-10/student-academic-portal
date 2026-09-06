//package com.studentportal.studentacademicportal.controller;
//
//public class StudentController {
//}
package com.studentportal.studentacademicportal.controller;
import java.util.List;
import com.studentportal.studentacademicportal.entity.Student;
import com.studentportal.studentacademicportal.service.StudentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/api/students")
    public Student createStudent(@RequestBody Student student) {
        return studentService.saveStudent(student);
    }
    @GetMapping("/api/students")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }
    @GetMapping("/api/students/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {

        Optional<Student> student = studentService.getStudentById(id);

        if (student.isPresent()) {
            return ResponseEntity.ok(student.get());
        }

        return ResponseEntity.notFound().build();
    }


//    Controller
//   ↓
//studentService.deleteStudent(1)
//   ↓
//studentRepository.deleteById(1)
//   ↓
//MySQL deletes student 1
//   ↓
//HTTP 204 No Content
    @DeleteMapping("/api/students/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {

        studentService.deleteStudent(id);

        return ResponseEntity.noContent().build();
    }
//HTML + JavaScript
//       ↓
//    REST API
//       ↓
//  Controller
//       ↓
//    Service
//       ↓
//  Repository
//       ↓
//     MySQL

    @PutMapping("/api/students/{id}")
    public ResponseEntity<Student> updateStudent(
            @PathVariable Long id,
            @RequestBody Student student) {

        Student updatedStudent = studentService.updateStudent(id, student);

        if (updatedStudent != null) {
            return ResponseEntity.ok(updatedStudent);
        }

        return ResponseEntity.notFound().build();
    }
}