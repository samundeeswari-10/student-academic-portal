package com.studentportal.studentacademicportal.dto;

public class LoginResponse {

    private String token;
    private Long id;
    private Long studentId;
    private String name;
    private String email;
    private String role;

    public LoginResponse(
            String token,
            Long id,
            Long studentId,
            String name,
            String email,
            String role) {

        this.token = token;
        this.id = id;
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public Long getId() {
        return id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}