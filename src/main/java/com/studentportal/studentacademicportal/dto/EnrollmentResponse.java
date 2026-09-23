package com.studentportal.studentacademicportal.dto;

public class EnrollmentResponse {

    private Long id;

    private Long studentId;
    private String studentName;
    private String studentEmail;

    private Long subjectId;
    private String subjectName;
    private String subjectCode;

    public EnrollmentResponse(
            Long id,
            Long studentId,
            String studentName,
            String studentEmail,
            Long subjectId,
            String subjectName,
            String subjectCode) {

        this.id = id;

        this.studentId = studentId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;

        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
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

    public String getStudentEmail() {
        return studentEmail;
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
}