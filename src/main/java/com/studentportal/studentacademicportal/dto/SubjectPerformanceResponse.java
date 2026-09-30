package com.studentportal.studentacademicportal.dto;

public class SubjectPerformanceResponse {

    private Long subjectId;
    private String subjectName;
    private String subjectCode;

    private double totalMarks;
    private double marksPercentage;
    private double attendancePercentage;

    public SubjectPerformanceResponse(
            Long subjectId,
            String subjectName,
            String subjectCode,
            double totalMarks,
            double marksPercentage,
            double attendancePercentage) {

        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.totalMarks = totalMarks;
        this.marksPercentage = marksPercentage;
        this.attendancePercentage = attendancePercentage;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public double getTotalMarks() {
        return totalMarks;
    }

    public double getMarksPercentage() {
        return marksPercentage;
    }

    public double getAttendancePercentage() {
        return attendancePercentage;
    }
}