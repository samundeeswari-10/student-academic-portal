package com.studentportal.studentacademicportal.dto;

import java.time.LocalDate;

public class AttendanceResponse {

    private Long id;

    private Long studentId;
    private String studentName;

    private Long subjectId;
    private String subjectName;
    private String subjectCode;

    private LocalDate attendanceDate;
    private boolean present;

    public AttendanceResponse(
            Long id,
            Long studentId,
            String studentName,
            Long subjectId,
            String subjectName,
            String subjectCode,
            LocalDate attendanceDate,
            boolean present) {

        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.attendanceDate = attendanceDate;
        this.present = present;
    }

    public Long getId() {
        return id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
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

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public boolean isPresent() {
        return present;
    }
}