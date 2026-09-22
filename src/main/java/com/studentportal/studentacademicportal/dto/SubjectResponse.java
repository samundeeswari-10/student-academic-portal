package com.studentportal.studentacademicportal.dto;

public class SubjectResponse {

    private Long id;
    private String name;
    private String code;
    private int credits;

    private Long departmentId;
    private String departmentName;
    private String departmentCode;

    private Long facultyId;
    private String facultyName;
    private String facultyEmail;

    public SubjectResponse(
            Long id,
            String name,
            String code,
            int credits,
            Long departmentId,
            String departmentName,
            String departmentCode,
            Long facultyId,
            String facultyName,
            String facultyEmail) {

        this.id = id;
        this.name = name;
        this.code = code;
        this.credits = credits;

        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.departmentCode = departmentCode;

        this.facultyId = facultyId;
        this.facultyName = facultyName;
        this.facultyEmail = facultyEmail;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public int getCredits() {
        return credits;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public Long getFacultyId() {
        return facultyId;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public String getFacultyEmail() {
        return facultyEmail;
    }
}