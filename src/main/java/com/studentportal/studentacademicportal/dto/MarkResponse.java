package com.studentportal.studentacademicportal.dto;

public class MarkResponse {

    private Long id;

    private Long studentId;
    private String studentName;

    private Long subjectId;
    private String subjectName;
    private String subjectCode;

    private double internalMarks;
    private double assignmentMarks;
    private double examMarks;

    public MarkResponse(
            Long id,
            Long studentId,
            String studentName,
            Long subjectId,
            String subjectName,
            String subjectCode,
            double internalMarks,
            double assignmentMarks,
            double examMarks) {

        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.internalMarks = internalMarks;
        this.assignmentMarks = assignmentMarks;
        this.examMarks = examMarks;
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

    public double getInternalMarks() {
        return internalMarks;
    }

    public double getAssignmentMarks() {
        return assignmentMarks;
    }

    public double getExamMarks() {
        return examMarks;
    }
}