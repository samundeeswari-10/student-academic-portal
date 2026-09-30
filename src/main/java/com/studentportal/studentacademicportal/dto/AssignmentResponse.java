package com.studentportal.studentacademicportal.dto;

import java.time.LocalDate;

public class AssignmentResponse {

    private Long id;

    private String title;
    private String description;
    private LocalDate dueDate;

    private Long subjectId;
    private String subjectName;
    private String subjectCode;

    public AssignmentResponse(
            Long id,
            String title,
            String description,
            LocalDate dueDate,
            Long subjectId,
            String subjectName,
            String subjectCode) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDueDate() {
        return dueDate;
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