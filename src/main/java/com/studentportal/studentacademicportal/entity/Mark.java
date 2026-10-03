
package com.studentportal.studentacademicportal.entity;

import jakarta.persistence.*;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"student_id", "subject_id"}
                )
        }
)
public class Mark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(nullable = false)
    private Double cat1;

    @Column(nullable = false)
    private Double cat2;

    @Column(nullable = false)
    private Double cat3;

    @Column(nullable = false)
    private Double assignment1;

    @Column(nullable = false)
    private Double assignment2;

    @Column(nullable = false)
    private Double assignment3;

    @Column(nullable = false)
    private Double finalExam;

    // Default constructor
    public Mark() {
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
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
}
