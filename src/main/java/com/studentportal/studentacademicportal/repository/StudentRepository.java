package com.studentportal.studentacademicportal.repository;

import com.studentportal.studentacademicportal.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}