package com.studentportal.studentacademicportal.dto;

import java.util.List;

public class StudentAnalyticsResponse {

    private Long studentId;
    private String studentName;

    private double overallMarksAverage;
    private double overallAttendancePercentage;

    private String academicRisk;

    private List<SubjectPerformanceResponse> subjectPerformance;

    public StudentAnalyticsResponse(
            Long studentId,
            String studentName,
            double overallMarksAverage,
            double overallAttendancePercentage,
            String academicRisk,
            List<SubjectPerformanceResponse> subjectPerformance) {

        this.studentId = studentId;
        this.studentName = studentName;
        this.overallMarksAverage = overallMarksAverage;
        this.overallAttendancePercentage = overallAttendancePercentage;
        this.academicRisk = academicRisk;
        this.subjectPerformance = subjectPerformance;
    }

    public Long getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public double getOverallMarksAverage() {
        return overallMarksAverage;
    }

    public double getOverallAttendancePercentage() {
        return overallAttendancePercentage;
    }

    public String getAcademicRisk() {
        return academicRisk;
    }

    public List<SubjectPerformanceResponse> getSubjectPerformance() {
        return subjectPerformance;
    }
}