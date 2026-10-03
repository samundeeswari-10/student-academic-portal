
package com.studentportal.studentacademicportal.dto;

public class MarkResponse {

    private Long id;
    private Long studentId;
    private String studentName;
    private Long subjectId;
    private String subjectName;
    private String subjectCode;

    private Double cat1;
    private Double cat2;
    private Double cat3;

    private Double assignment1;
    private Double assignment2;
    private Double assignment3;

    private Double finalExam;

    private Double catAverage;
    private Double assignmentAverage;
    private Double internalTotal;
    private Double finalExamConverted;
    private Double overallTotal;

    public MarkResponse() {
    }

    public MarkResponse(
            Long id,
            Long studentId,
            String studentName,
            Long subjectId,
            String subjectName,
            String subjectCode,
            Double cat1,
            Double cat2,
            Double cat3,
            Double assignment1,
            Double assignment2,
            Double assignment3,
            Double finalExam,
            Double catAverage,
            Double assignmentAverage,
            Double internalTotal,
            Double finalExamConverted,
            Double overallTotal) {

        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;

        this.cat1 = cat1;
        this.cat2 = cat2;
        this.cat3 = cat3;

        this.assignment1 = assignment1;
        this.assignment2 = assignment2;
        this.assignment3 = assignment3;

        this.finalExam = finalExam;

        this.catAverage = catAverage;
        this.assignmentAverage = assignmentAverage;
        this.internalTotal = internalTotal;
        this.finalExamConverted = finalExamConverted;
        this.overallTotal = overallTotal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public Double getCat1() {
        return cat1;
    }

    public void setCat1(Double cat1) {
        this.cat1 = cat1;
    }

    public Double getCat2() {
        return cat2;
    }

    public void setCat2(Double cat2) {
        this.cat2 = cat2;
    }

    public Double getCat3() {
        return cat3;
    }

    public void setCat3(Double cat3) {
        this.cat3 = cat3;
    }

    public Double getAssignment1() {
        return assignment1;
    }

    public void setAssignment1(Double assignment1) {
        this.assignment1 = assignment1;
    }

    public Double getAssignment2() {
        return assignment2;
    }

    public void setAssignment2(Double assignment2) {
        this.assignment2 = assignment2;
    }

    public Double getAssignment3() {
        return assignment3;
    }

    public void setAssignment3(Double assignment3) {
        this.assignment3 = assignment3;
    }

    public Double getFinalExam() {
        return finalExam;
    }

    public void setFinalExam(Double finalExam) {
        this.finalExam = finalExam;
    }

    public Double getCatAverage() {
        return catAverage;
    }

    public void setCatAverage(Double catAverage) {
        this.catAverage = catAverage;
    }

    public Double getAssignmentAverage() {
        return assignmentAverage;
    }

    public void setAssignmentAverage(Double assignmentAverage) {
        this.assignmentAverage = assignmentAverage;
    }

    public Double getInternalTotal() {
        return internalTotal;
    }

    public void setInternalTotal(Double internalTotal) {
        this.internalTotal = internalTotal;
    }

    public Double getFinalExamConverted() {
        return finalExamConverted;
    }

    public void setFinalExamConverted(Double finalExamConverted) {
        this.finalExamConverted = finalExamConverted;
    }

    public Double getOverallTotal() {
        return overallTotal;
    }

    public void setOverallTotal(Double overallTotal) {
        this.overallTotal = overallTotal;
    }
}
