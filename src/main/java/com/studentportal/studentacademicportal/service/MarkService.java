
package com.studentportal.studentacademicportal.service;

import com.studentportal.studentacademicportal.entity.Mark;
import com.studentportal.studentacademicportal.entity.Student;
import com.studentportal.studentacademicportal.entity.Subject;
import com.studentportal.studentacademicportal.repository.MarkRepository;
import com.studentportal.studentacademicportal.repository.StudentRepository;
import com.studentportal.studentacademicportal.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MarkService {

    private final MarkRepository markRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;

    public MarkService(
            MarkRepository markRepository,
            StudentRepository studentRepository,
            SubjectRepository subjectRepository) {

        this.markRepository = markRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
    }

    // Validate all marks before saving
    private void validateMarks(Mark mark) {

        validateRange(mark.getCat1(), 50, "CAT 1");
        validateRange(mark.getCat2(), 50, "CAT 2");
        validateRange(mark.getCat3(), 50, "CAT 3");

        validateRange(mark.getAssignment1(), 10, "Assignment 1");
        validateRange(mark.getAssignment2(), 10, "Assignment 2");
        validateRange(mark.getAssignment3(), 10, "Assignment 3");

        validateRange(mark.getFinalExam(), 100, "Final Exam");
    }

    private void validateRange(
            Double marks,
            double maximum,
            String fieldName) {

        if (marks == null || !Double.isFinite(marks)
                || marks < 0 || marks > maximum) {

            throw new IllegalArgumentException(
                    fieldName + " must be between 0 and " + maximum
            );
        }
    }

    // CAT average out of 50
    public double calculateCatAverage(Mark mark) {
        return (mark.getCat1()
                + mark.getCat2()
                + mark.getCat3()) / 3.0;
    }

    // Convert CAT average from 50 to 30
    public double calculateCatScore(Mark mark) {
        return calculateCatAverage(mark) * 30.0 / 50.0;
    }

    // Assignment average out of 10
    public double calculateAssignmentAverage(Mark mark) {
        return (mark.getAssignment1()
                + mark.getAssignment2()
                + mark.getAssignment3()) / 3.0;
    }

    // Internal total out of 40
    public double calculateInternalTotal(Mark mark) {
        return calculateCatScore(mark)
                + calculateAssignmentAverage(mark);
    }

    // Convert final exam from 100 to 60
    public double calculateFinalExamConverted(Mark mark) {
        return mark.getFinalExam() * 60.0 / 100.0;
    }

    // Overall total out of 100
    public double calculateOverallTotal(Mark mark) {
        return calculateInternalTotal(mark)
                + calculateFinalExamConverted(mark);
    }

    // Create marks
    @Transactional
    public Mark createMark(
            Long studentId,
            Long subjectId,
            Mark mark) {

        validateMarks(mark);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() ->
                        new RuntimeException("Subject not found"));

        if (markRepository.existsByStudentIdAndSubjectId(
                studentId, subjectId)) {

            throw new RuntimeException(
                    "Marks already exist for this student and subject");
        }

        mark.setStudent(student);
        mark.setSubject(subject);

        return markRepository.save(mark);
    }

    // Get marks by student
    public List<Mark> getMarksByStudent(Long studentId) {
        return markRepository.findByStudentId(studentId);
    }

    // Get marks by subject
    public List<Mark> getMarksBySubject(Long subjectId) {
        return markRepository.findBySubjectId(subjectId);
    }

    // Get marks by ID
    public Optional<Mark> getMarkById(Long id) {
        return markRepository.findById(id);
    }

    // Update marks
    @Transactional
    public Mark updateMark(
            Long id,
            Mark updatedMark) {

        validateMarks(updatedMark);

        Optional<Mark> existing =
                markRepository.findById(id);

        if (existing.isEmpty()) {
            return null;
        }

        Mark mark = existing.get();

        mark.setCat1(updatedMark.getCat1());
        mark.setCat2(updatedMark.getCat2());
        mark.setCat3(updatedMark.getCat3());

        mark.setAssignment1(updatedMark.getAssignment1());
        mark.setAssignment2(updatedMark.getAssignment2());
        mark.setAssignment3(updatedMark.getAssignment3());

        mark.setFinalExam(updatedMark.getFinalExam());

        return markRepository.save(mark);
    }

    // Delete marks
    @Transactional
    public boolean deleteMark(Long id) {

        if (!markRepository.existsById(id)) {
            return false;
        }

        markRepository.deleteById(id);
        return true;
    }
}
