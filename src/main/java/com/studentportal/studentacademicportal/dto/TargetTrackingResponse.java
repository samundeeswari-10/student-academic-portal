
package com.studentportal.studentacademicportal.dto;

public class TargetTrackingResponse {

    private Long studentId;
    private double currentAverage;
    private double targetPercentage;
    private double percentageGap;
    private String targetStatus;

    public TargetTrackingResponse(
            Long studentId,
            double currentAverage,
            double targetPercentage,
            double percentageGap,
            String targetStatus) {

        this.studentId = studentId;
        this.currentAverage = currentAverage;
        this.targetPercentage = targetPercentage;
        this.percentageGap = percentageGap;
        this.targetStatus = targetStatus;
    }

    public Long getStudentId() {
        return studentId;
    }

    public double getCurrentAverage() {
        return currentAverage;
    }

    public double getTargetPercentage() {
        return targetPercentage;
    }

    public double getPercentageGap() {
        return percentageGap;
    }

    public String getTargetStatus() {
        return targetStatus;
    }
}
