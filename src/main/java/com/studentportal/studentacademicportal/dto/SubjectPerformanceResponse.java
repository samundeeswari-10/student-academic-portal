
package com.studentportal.studentacademicportal.dto;

public class SubjectPerformanceResponse {

    private Long subjectId;
    private String subjectName;
    private String subjectCode;

    private double totalMarks;
    private double marksPercentage;
    private double attendancePercentage;

    private double cat1;
    private double cat2;
    private double cat3;
    private double catAverage;
    private double progressChange;
    private String progressTrend;

    private int classesAttended;
    private int totalClasses;
    private int classesNeededFor75Percent;

    public SubjectPerformanceResponse(
            Long subjectId,
            String subjectName,
            String subjectCode,
            double totalMarks,
            double marksPercentage,
            double attendancePercentage,
            double cat1,
            double cat2,
            double cat3,
            double catAverage,
            double progressChange,
            String progressTrend,
            int classesAttended,
            int totalClasses,
            int classesNeededFor75Percent) {

        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.totalMarks = totalMarks;
        this.marksPercentage = marksPercentage;
        this.attendancePercentage = attendancePercentage;
        this.cat1 = cat1;
        this.cat2 = cat2;
        this.cat3 = cat3;
        this.catAverage = catAverage;
        this.progressChange = progressChange;
        this.progressTrend = progressTrend;
        this.classesAttended = classesAttended;
        this.totalClasses = totalClasses;
        this.classesNeededFor75Percent = classesNeededFor75Percent;
    }

    // Compatibility constructor for existing code
    public SubjectPerformanceResponse(
            Long subjectId,
            String subjectName,
            String subjectCode,
            double totalMarks,
            double marksPercentage,
            double attendancePercentage,
            double cat1,
            double cat2,
            double cat3,
            double catAverage,
            double progressChange,
            String progressTrend) {

        this(subjectId, subjectName, subjectCode,
                totalMarks, marksPercentage, attendancePercentage,
                cat1, cat2, cat3, catAverage,
                progressChange, progressTrend, 0, 0, 0);
    }

    // Original compatibility constructor
    public SubjectPerformanceResponse(
            Long subjectId,
            String subjectName,
            String subjectCode,
            double totalMarks,
            double marksPercentage,
            double attendancePercentage) {

        this(subjectId, subjectName, subjectCode,
                totalMarks, marksPercentage, attendancePercentage,
                0, 0, 0, 0, 0, "NOT_AVAILABLE", 0, 0, 0);
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

    public double getCat1() {
        return cat1;
    }

    public double getCat2() {
        return cat2;
    }

    public double getCat3() {
        return cat3;
    }

    public double getCatAverage() {
        return catAverage;
    }

    public double getProgressChange() {
        return progressChange;
    }

    public String getProgressTrend() {
        return progressTrend;
    }

    public int getClassesAttended() {
        return classesAttended;
    }

    public int getTotalClasses() {
        return totalClasses;
    }

    public int getClassesNeededFor75Percent() {
        return classesNeededFor75Percent;
    }
}
