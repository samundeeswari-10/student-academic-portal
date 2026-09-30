package com.studentportal.studentacademicportal.service;

import com.studentportal.studentacademicportal.entity.Department;
import com.studentportal.studentacademicportal.entity.Subject;
import com.studentportal.studentacademicportal.entity.User;
import com.studentportal.studentacademicportal.repository.DepartmentRepository;
import com.studentportal.studentacademicportal.repository.SubjectRepository;
import com.studentportal.studentacademicportal.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.studentportal.studentacademicportal.entity.Assignment;
import com.studentportal.studentacademicportal.repository.AssignmentRepository;
import com.studentportal.studentacademicportal.repository.AssignmentSubmissionRepository;
import com.studentportal.studentacademicportal.repository.AttendanceRepository;
import com.studentportal.studentacademicportal.repository.EnrollmentRepository;
import com.studentportal.studentacademicportal.repository.MarkRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;
    private final MarkRepository markRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AssignmentRepository assignmentRepository;
    private final AssignmentSubmissionRepository assignmentSubmissionRepository;

    public SubjectService(
            SubjectRepository subjectRepository,
            DepartmentRepository departmentRepository,
            UserRepository userRepository,
            AttendanceRepository attendanceRepository,
            MarkRepository markRepository,
            EnrollmentRepository enrollmentRepository,
            AssignmentRepository assignmentRepository,
            AssignmentSubmissionRepository assignmentSubmissionRepository) {

        this.subjectRepository = subjectRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.attendanceRepository = attendanceRepository;
        this.markRepository = markRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.assignmentRepository = assignmentRepository;
        this.assignmentSubmissionRepository = assignmentSubmissionRepository;
    }

    public Subject createSubject(
            Subject subject,
            Long departmentId,
            Long facultyId) {

        Department department =
                departmentRepository.findById(departmentId)
                        .orElseThrow(() ->
                                new RuntimeException("Department not found"));

        User faculty =
                userRepository.findById(facultyId)
                        .orElseThrow(() ->
                                new RuntimeException("Faculty not found"));

        if (!faculty.getRole().equals("FACULTY")) {
            throw new RuntimeException(
                    "Selected user is not a faculty member");
        }

        subject.setDepartment(department);
        subject.setFaculty(faculty);

        return subjectRepository.save(subject);
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public Optional<Subject> getSubjectById(Long id) {
        return subjectRepository.findById(id);
    }

    public List<Subject> getSubjectsByDepartment(Long departmentId) {
        return subjectRepository.findByDepartmentId(departmentId);
    }

    public Subject updateSubject(
            Long id,
            Subject updatedSubject,
            Long departmentId,
            Long facultyId) {

        Optional<Subject> existing =
                subjectRepository.findById(id);

        if (existing.isEmpty()) {
            return null;
        }

        Department department =
                departmentRepository.findById(departmentId)
                        .orElseThrow(() ->
                                new RuntimeException("Department not found"));

        User faculty =
                userRepository.findById(facultyId)
                        .orElseThrow(() ->
                                new RuntimeException("Faculty not found"));

        if (!faculty.getRole().equals("FACULTY")) {
            throw new RuntimeException(
                    "Selected user is not a faculty member");
        }

        Subject subject = existing.get();

        subject.setName(updatedSubject.getName());
        subject.setCode(updatedSubject.getCode());
        subject.setCredits(updatedSubject.getCredits());
        subject.setDepartment(department);
        subject.setFaculty(faculty);

        return subjectRepository.save(subject);
    }

    @Transactional
    public boolean deleteSubject(Long id) {

        if (!subjectRepository.existsById(id)) {
            return false;
        }

        // 1. Delete attendance records
        attendanceRepository
                .deleteAll(attendanceRepository.findBySubjectId(id));

        // 2. Delete marks
        markRepository
                .deleteAll(markRepository.findBySubjectId(id));

        // 3. Delete enrollments
        enrollmentRepository
                .deleteAll(enrollmentRepository.findBySubjectId(id));

        // 4. Delete assignment submissions
        List<Assignment> assignments =
                assignmentRepository.findBySubjectId(id);

        for (Assignment assignment : assignments) {
            assignmentSubmissionRepository
                    .deleteAll(
                            assignmentSubmissionRepository
                                    .findByAssignmentId(assignment.getId())
                    );
        }

        // 5. Delete assignments
        assignmentRepository.deleteAll(assignments);

        // 6. Finally delete the subject
        subjectRepository.deleteById(id);

        return true;
    }
}