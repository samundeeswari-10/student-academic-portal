package com.studentportal.studentacademicportal.service;

import com.studentportal.studentacademicportal.entity.Department;
import com.studentportal.studentacademicportal.entity.Subject;
import com.studentportal.studentacademicportal.entity.User;
import com.studentportal.studentacademicportal.repository.DepartmentRepository;
import com.studentportal.studentacademicportal.repository.SubjectRepository;
import com.studentportal.studentacademicportal.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    public SubjectService(
            SubjectRepository subjectRepository,
            DepartmentRepository departmentRepository,
            UserRepository userRepository) {

        this.subjectRepository = subjectRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
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

    public boolean deleteSubject(Long id) {

        if (!subjectRepository.existsById(id)) {
            return false;
        }

        subjectRepository.deleteById(id);
        return true;
    }
}