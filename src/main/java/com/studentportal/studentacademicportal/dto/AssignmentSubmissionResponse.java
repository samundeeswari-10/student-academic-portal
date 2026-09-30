package com.studentportal.studentacademicportal.dto;

import java.time.LocalDate;

public class AssignmentSubmissionResponse {

    private Long id;

    private Long studentId;
    private String studentName;

    private Long assignmentId;
    private String assignmentTitle;

    private String content;
    private LocalDate submittedDate;
    private String status;

    public AssignmentSubmissionResponse(
            Long id,
            Long studentId,
            String studentName,
            Long assignmentId,
            String assignmentTitle,
            String content,
            LocalDate submittedDate,
            String status) {

        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.assignmentId = assignmentId;
        this.assignmentTitle = assignmentTitle;
        this.content = content;
        this.submittedDate = submittedDate;
        this.status = status;
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

    public Long getAssignmentId() {
        return assignmentId;
    }

    public String getAssignmentTitle() {
        return assignmentTitle;
    }

    public String getContent() {
        return content;
    }

    public LocalDate getSubmittedDate() {
        return submittedDate;
    }

    public String getStatus() {
        return status;
    }
}