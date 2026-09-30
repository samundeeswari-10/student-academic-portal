package com.studentportal.studentacademicportal.service;

import com.studentportal.studentacademicportal.entity.Assignment;
import com.studentportal.studentacademicportal.entity.AssignmentSubmission;
import com.studentportal.studentacademicportal.entity.Student;
import com.studentportal.studentacademicportal.repository.AssignmentRepository;
import com.studentportal.studentacademicportal.repository.AssignmentSubmissionRepository;
import com.studentportal.studentacademicportal.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AssignmentSubmissionService {

    private final AssignmentSubmissionRepository submissionRepository;
    private final StudentRepository studentRepository;
    private final AssignmentRepository assignmentRepository;

    public AssignmentSubmissionService(
            AssignmentSubmissionRepository submissionRepository,
            StudentRepository studentRepository,
            AssignmentRepository assignmentRepository) {

        this.submissionRepository = submissionRepository;
        this.studentRepository = studentRepository;
        this.assignmentRepository = assignmentRepository;
    }

    public AssignmentSubmission submitAssignment(
            Long studentId,
            Long assignmentId,
            AssignmentSubmission submission) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() ->
                        new RuntimeException("Assignment not found"));

        if (submissionRepository
                .existsByStudentIdAndAssignmentId(
                        studentId,
                        assignmentId)) {

            throw new RuntimeException(
                    "Student has already submitted this assignment");
        }

        submission.setStudent(student);
        submission.setAssignment(assignment);

        if (submission.getSubmittedDate() == null) {
            submission.setSubmittedDate(LocalDate.now());
        }

        if (submission.getSubmittedDate()
                .isAfter(assignment.getDueDate())) {

            submission.setStatus("LATE");

        } else {

            submission.setStatus("SUBMITTED");
        }

        return submissionRepository.save(submission);
    }

    public List<AssignmentSubmission> getSubmissionsByStudent(
            Long studentId) {

        return submissionRepository.findByStudentId(studentId);
    }

    public List<AssignmentSubmission> getSubmissionsByAssignment(
            Long assignmentId) {

        return submissionRepository.findByAssignmentId(assignmentId);
    }

    public Optional<AssignmentSubmission> getSubmissionById(
            Long id) {

        return submissionRepository.findById(id);
    }

    public AssignmentSubmission updateSubmission(
            Long id,
            AssignmentSubmission updatedSubmission) {

        Optional<AssignmentSubmission> existing =
                submissionRepository.findById(id);

        if (existing.isEmpty()) {
            return null;
        }

        AssignmentSubmission submission = existing.get();

        submission.setContent(updatedSubmission.getContent());

        if (updatedSubmission.getSubmittedDate() != null) {
            submission.setSubmittedDate(
                    updatedSubmission.getSubmittedDate());
        }

        Assignment assignment = submission.getAssignment();

        if (submission.getSubmittedDate()
                .isAfter(assignment.getDueDate())) {

            submission.setStatus("LATE");

        } else {

            submission.setStatus("SUBMITTED");
        }

        return submissionRepository.save(submission);
    }

    public boolean deleteSubmission(Long id) {

        if (!submissionRepository.existsById(id)) {
            return false;
        }

        submissionRepository.deleteById(id);
        return true;
    }
}