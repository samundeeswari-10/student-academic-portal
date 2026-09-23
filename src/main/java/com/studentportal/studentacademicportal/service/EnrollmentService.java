package com.studentportal.studentacademicportal.service;

import com.studentportal.studentacademicportal.entity.Enrollment;
import com.studentportal.studentacademicportal.entity.Student;
import com.studentportal.studentacademicportal.entity.Subject;
import com.studentportal.studentacademicportal.repository.EnrollmentRepository;
import com.studentportal.studentacademicportal.repository.StudentRepository;
import com.studentportal.studentacademicportal.repository.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;

    public EnrollmentService(
            EnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository,
            SubjectRepository subjectRepository) {

        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
    }

    public Enrollment enrollStudent(
            Long studentId,
            Long subjectId) {

        Student student =
                studentRepository.findById(studentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Student not found"));

        Subject subject =
                subjectRepository.findById(subjectId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Subject not found"));

        if (enrollmentRepository
                .existsByStudentIdAndSubjectId(
                        studentId,
                        subjectId)) {

            throw new RuntimeException(
                    "Student is already enrolled in this subject");
        }

        Enrollment enrollment = new Enrollment();

        enrollment.setStudent(student);
        enrollment.setSubject(subject);

        return enrollmentRepository.save(enrollment);
    }

    public List<Enrollment> getEnrollmentsByStudent(
            Long studentId) {

        return enrollmentRepository
                .findByStudentId(studentId);
    }

    public List<Enrollment> getEnrollmentsBySubject(
            Long subjectId) {

        return enrollmentRepository
                .findBySubjectId(subjectId);
    }

    public Optional<Enrollment> getEnrollmentById(
            Long id) {

        return enrollmentRepository.findById(id);
    }

    public boolean deleteEnrollment(Long id) {

        if (!enrollmentRepository.existsById(id)) {
            return false;
        }

        enrollmentRepository.deleteById(id);
        return true;
    }
}