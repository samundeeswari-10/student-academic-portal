package com.studentportal.studentacademicportal.service;

import com.studentportal.studentacademicportal.entity.Assignment;
import com.studentportal.studentacademicportal.entity.Subject;
import com.studentportal.studentacademicportal.repository.AssignmentRepository;
import com.studentportal.studentacademicportal.repository.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final SubjectRepository subjectRepository;

    public AssignmentService(
            AssignmentRepository assignmentRepository,
            SubjectRepository subjectRepository) {

        this.assignmentRepository = assignmentRepository;
        this.subjectRepository = subjectRepository;
    }

    public Assignment createAssignment(
            Long subjectId,
            Assignment assignment) {

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() ->
                        new RuntimeException("Subject not found"));

        assignment.setSubject(subject);

        return assignmentRepository.save(assignment);
    }

    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    public List<Assignment> getAssignmentsBySubject(Long subjectId) {
        return assignmentRepository.findBySubjectId(subjectId);
    }

    public Optional<Assignment> getAssignmentById(Long id) {
        return assignmentRepository.findById(id);
    }

    public Assignment updateAssignment(
            Long id,
            Assignment updatedAssignment) {

        Optional<Assignment> existing =
                assignmentRepository.findById(id);

        if (existing.isEmpty()) {
            return null;
        }

        Assignment assignment = existing.get();

        assignment.setTitle(updatedAssignment.getTitle());
        assignment.setDescription(updatedAssignment.getDescription());
        assignment.setDueDate(updatedAssignment.getDueDate());

        return assignmentRepository.save(assignment);
    }

    public boolean deleteAssignment(Long id) {

        if (!assignmentRepository.existsById(id)) {
            return false;
        }

        assignmentRepository.deleteById(id);
        return true;
    }
}