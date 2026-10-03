
package com.studentportal.studentacademicportal.service;

import com.studentportal.studentacademicportal.dto.StudentAnalyticsResponse;
import com.studentportal.studentacademicportal.dto.SubjectPerformanceResponse;
import com.studentportal.studentacademicportal.entity.Attendance;
import com.studentportal.studentacademicportal.entity.Mark;
import com.studentportal.studentacademicportal.entity.Student;
import com.studentportal.studentacademicportal.entity.Subject;
import com.studentportal.studentacademicportal.repository.AttendanceRepository;
import com.studentportal.studentacademicportal.repository.MarkRepository;
import com.studentportal.studentacademicportal.repository.StudentRepository;
import org.springframework.stereotype.Service;
import com.studentportal.studentacademicportal.dto.TargetTrackingResponse;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    private static final double ATTENDANCE_TARGET = 75.0;

    private final StudentRepository studentRepository;
    private final MarkRepository markRepository;
    private final AttendanceRepository attendanceRepository;
    private final MarkService markService;

    public AnalyticsService(
            StudentRepository studentRepository,
            MarkRepository markRepository,
            AttendanceRepository attendanceRepository,
            MarkService markService) {

        this.studentRepository = studentRepository;
        this.markRepository = markRepository;
        this.attendanceRepository = attendanceRepository;
        this.markService = markService;
    }

    public StudentAnalyticsResponse getStudentAnalytics(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        List<Mark> marks = markRepository.findByStudentId(studentId);
        List<Attendance> attendance =
                attendanceRepository.findByStudentId(studentId);

        // Keep subjects in insertion order and include those with
        // either marks or attendance records.
        Map<Long, Subject> subjects = new LinkedHashMap<>();
        Map<Long, Mark> marksBySubject = new LinkedHashMap<>();

        for (Mark mark : marks) {
            if (mark.getSubject() != null) {
                Long subjectId = mark.getSubject().getId();
                subjects.putIfAbsent(subjectId, mark.getSubject());
                marksBySubject.put(subjectId, mark);
            }
        }

        for (Attendance record : attendance) {
            if (record.getSubject() != null) {
                Subject subject = record.getSubject();
                subjects.putIfAbsent(subject.getId(), subject);
            }
        }

        List<SubjectPerformanceResponse> subjectPerformance =
                new ArrayList<>();

        double totalMarks = 0;
        int subjectsWithMarks = 0;
        int totalAttendanceClasses = attendance.size();
        int totalPresentClasses = 0;

        for (Attendance record : attendance) {
            if (record.isPresent()) {
                totalPresentClasses++;
            }
        }

        for (Map.Entry<Long, Subject> entry : subjects.entrySet()) {

            Long subjectId = entry.getKey();
            Subject subject = entry.getValue();
            Mark mark = marksBySubject.get(subjectId);

            // Default values for subjects without marks
            double subjectTotal = 0;
            double cat1 = 0;
            double cat2 = 0;
            double cat3 = 0;
            double catAverage = 0;
            double progressChange = 0;
            String progressTrend = "NOT_AVAILABLE";

            if (mark != null) {
                subjectTotal =
                        markService.calculateOverallTotal(mark);

                Double cat1Value = mark.getCat1();
                Double cat2Value = mark.getCat2();
                Double cat3Value = mark.getCat3();

                cat1 = cat1Value != null ? cat1Value : 0;
                cat2 = cat2Value != null ? cat2Value : 0;
                cat3 = cat3Value != null ? cat3Value : 0;

                catAverage = (cat1 + cat2 + cat3) / 3.0;

                if (cat1Value != null && cat3Value != null) {
                    progressChange = cat3 - cat1;

                    if (progressChange > 0) {
                        progressTrend = "IMPROVING";
                    } else if (progressChange < 0) {
                        progressTrend = "DECLINING";
                    } else {
                        progressTrend = "STABLE";
                    }
                }

                totalMarks += subjectTotal;
                subjectsWithMarks++;
            }

            int subjectTotalClasses = 0;
            int subjectPresentClasses = 0;

            for (Attendance record : attendance) {
                if (record.getSubject() != null
                        && subjectId.equals(record.getSubject().getId())) {

                    subjectTotalClasses++;

                    if (record.isPresent()) {
                        subjectPresentClasses++;
                    }
                }
            }

            double attendancePercentage = 0;

            if (subjectTotalClasses > 0) {
                attendancePercentage =
                        (double) subjectPresentClasses
                                / subjectTotalClasses * 100;
            }

            // Minimum consecutive classes to attend to reach 75%.
            // If there are no records, the value is zero because
            // attendance cannot yet be assessed.
            int classesNeededFor75Percent = 0;

            if (subjectTotalClasses > 0
                    && attendancePercentage < ATTENDANCE_TARGET) {

                classesNeededFor75Percent = (int) Math.ceil(
                        (ATTENDANCE_TARGET * subjectTotalClasses
                                - 100.0 * subjectPresentClasses)
                                / (100.0 - ATTENDANCE_TARGET)
                );

                classesNeededFor75Percent =
                        Math.max(0, classesNeededFor75Percent);
            }

            subjectPerformance.add(
                    new SubjectPerformanceResponse(
                            subjectId,
                            subject.getName(),
                            subject.getCode(),
                            subjectTotal,
                            subjectTotal,
                            attendancePercentage,
                            cat1,
                            cat2,
                            cat3,
                            catAverage,
                            progressChange,
                            progressTrend,
                            subjectPresentClasses,
                            subjectTotalClasses,
                            classesNeededFor75Percent
                    )
            );
        }

        double overallMarksAverage = 0;

        if (subjectsWithMarks > 0) {
            overallMarksAverage = totalMarks / subjectsWithMarks;
        }

        double overallAttendancePercentage = 0;

        if (totalAttendanceClasses > 0) {
            overallAttendancePercentage =
                    (double) totalPresentClasses
                            / totalAttendanceClasses * 100;
        }

        String academicRisk =
                calculateRisk(
                        overallMarksAverage,
                        overallAttendancePercentage,
                        subjectsWithMarks > 0,
                        totalAttendanceClasses > 0
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
            double attendancePercentage,
            boolean hasMarks,
            boolean hasAttendance) {

        // Avoid assigning a risk level when no analytics data exists.
        if (!hasMarks && !hasAttendance) {
            return "NOT_AVAILABLE";
        }

        if ((hasMarks && marksAverage < 50)
                || (hasAttendance && attendancePercentage < 75)) {
            return "HIGH";
        }

        if ((hasMarks && marksAverage < 65)
                || (hasAttendance && attendancePercentage < 80)) {
            return "MEDIUM";
        }

        return "LOW";
    }

    public TargetTrackingResponse getTargetTracking(
            Long studentId,
            double targetPercentage) {

        studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        if (!Double.isFinite(targetPercentage)
                || targetPercentage < 0
                || targetPercentage > 100) {
            throw new IllegalArgumentException(
                    "Target percentage must be between 0 and 100");
        }

        List<Mark> marks = markRepository.findByStudentId(studentId);

        double totalMarks = 0;

        for (Mark mark : marks) {
            totalMarks += markService.calculateOverallTotal(mark);
        }

        double currentAverage = 0;

        if (!marks.isEmpty()) {
            currentAverage = totalMarks / marks.size();
        }

        double percentageGap = targetPercentage - currentAverage;

        String targetStatus;

        if (percentageGap > 0) {
            targetStatus = "BELOW_TARGET";
        } else if (percentageGap < 0) {
            targetStatus = "TARGET_EXCEEDED";
        } else {
            targetStatus = "TARGET_MET";
        }

        return new TargetTrackingResponse(
                studentId,
                currentAverage,
                targetPercentage,
                percentageGap,
                targetStatus
        );
    }

}
