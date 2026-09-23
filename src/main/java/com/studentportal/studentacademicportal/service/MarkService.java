package com.studentportal.studentacademicportal.service;

import com.studentportal.studentacademicportal.entity.Mark;
import com.studentportal.studentacademicportal.entity.Student;
import com.studentportal.studentacademicportal.entity.Subject;
import com.studentportal.studentacademicportal.repository.MarkRepository;
import com.studentportal.studentacademicportal.repository.StudentRepository;
import com.studentportal.studentacademicportal.repository.SubjectRepository;
import org.springframework.stereotype.Service;

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

    public Mark createMark(
            Long studentId,
            Long subjectId,
            Mark mark) {

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

        if (markRepository
                .existsByStudentIdAndSubjectId(
                        studentId,
                        subjectId)) {

            throw new RuntimeException(
                    "Marks already exist for this student and subject");
        }

        mark.setStudent(student);
        mark.setSubject(subject);

        return markRepository.save(mark);
    }

    public List<Mark> getMarksByStudent(Long studentId) {
        return markRepository.findByStudentId(studentId);
    }

    public List<Mark> getMarksBySubject(Long subjectId) {
        return markRepository.findBySubjectId(subjectId);
    }

    public Optional<Mark> getMarkById(Long id) {
        return markRepository.findById(id);
    }

    public Mark updateMark(
            Long id,
            Mark updatedMark) {

        Optional<Mark> existing =
                markRepository.findById(id);

        if (existing.isEmpty()) {
            return null;
        }

        Mark mark = existing.get();

        mark.setInternalMarks(
                updatedMark.getInternalMarks());

        mark.setAssignmentMarks(
                updatedMark.getAssignmentMarks());

        mark.setExamMarks(
                updatedMark.getExamMarks());

        return markRepository.save(mark);
    }

    public boolean deleteMark(Long id) {

        if (!markRepository.existsById(id)) {
            return false;
        }

        markRepository.deleteById(id);
        return true;
    }
}