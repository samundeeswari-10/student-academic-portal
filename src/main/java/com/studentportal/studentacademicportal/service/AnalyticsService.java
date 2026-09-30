package com.studentportal.studentacademicportal.service;

import com.studentportal.studentacademicportal.dto.StudentAnalyticsResponse;
import com.studentportal.studentacademicportal.dto.SubjectPerformanceResponse;
import com.studentportal.studentacademicportal.entity.Attendance;
import com.studentportal.studentacademicportal.entity.Mark;
import com.studentportal.studentacademicportal.entity.Student;
import com.studentportal.studentacademicportal.repository.AttendanceRepository;
import com.studentportal.studentacademicportal.repository.MarkRepository;
import com.studentportal.studentacademicportal.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnalyticsService {

    private final StudentRepository studentRepository;
    private final MarkRepository markRepository;
    private final AttendanceRepository attendanceRepository;

    public AnalyticsService(
            StudentRepository studentRepository,
            MarkRepository markRepository,
            AttendanceRepository attendanceRepository) {

        this.studentRepository = studentRepository;
        this.markRepository = markRepository;
        this.attendanceRepository = attendanceRepository;
    }

    public StudentAnalyticsResponse getStudentAnalytics(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        List<Mark> marks = markRepository.findByStudentId(studentId);

        List<Attendance> attendance =
                attendanceRepository.findByStudentId(studentId);

        List<SubjectPerformanceResponse> subjectPerformance =
                new ArrayList<>();

        double totalMarks = 0;
        int totalAttendanceClasses = 0;
        int totalPresentClasses = 0;

        for (Mark mark : marks) {

            double subjectTotal =
                    mark.getInternalMarks()
                            + mark.getAssignmentMarks()
                            + mark.getExamMarks();

            double marksPercentage =
                    (subjectTotal / 130) * 100;

            Long subjectId = mark.getSubject().getId();

            int subjectTotalClasses =
                    (int) attendance.stream()
                            .filter(a ->
                                    a.getSubject().getId().equals(subjectId))
                            .count();

            int subjectPresentClasses =
                    (int) attendance.stream()
                            .filter(a ->
                                    a.getSubject().getId().equals(subjectId)
                                            && a.isPresent())
                            .count();

            double attendancePercentage = 0;

            if (subjectTotalClasses > 0) {
                attendancePercentage =
                        ((double) subjectPresentClasses
                                / subjectTotalClasses) * 100;
            }

            subjectPerformance.add(
                    new SubjectPerformanceResponse(
                            subjectId,
                            mark.getSubject().getName(),
                            mark.getSubject().getCode(),
                            subjectTotal,
                            marksPercentage,
                            attendancePercentage
                    )
            );

            totalMarks += subjectTotal;

            totalAttendanceClasses += subjectTotalClasses;
            totalPresentClasses += subjectPresentClasses;
        }

        double overallMarksAverage = 0;

        if (!marks.isEmpty()) {
            overallMarksAverage =
                    (totalMarks / (marks.size() * 130)) * 100;
        }

        double overallAttendancePercentage = 0;

        if (totalAttendanceClasses > 0) {
            overallAttendancePercentage =
                    ((double) totalPresentClasses
                            / totalAttendanceClasses) * 100;
        }

        String academicRisk =
                calculateRisk(
                        overallMarksAverage,
                        overallAttendancePercentage
                );

        return new StudentAnalyticsResponse(
                student.getId(),
                student.getName(),
                overallMarksAverage,
                overallAttendancePercentage,
                academicRisk,
                subjectPerformance
        );
    }

    private String calculateRisk(
            double marksAverage,
            double attendancePercentage) {

        if (marksAverage < 50 || attendancePercentage < 75) {
            return "HIGH";
        }

        if (marksAverage < 65 || attendancePercentage < 80) {
            return "MEDIUM";
        }

        return "LOW";
    }
}