package com.studentportal.studentacademicportal.repository;

import com.studentportal.studentacademicportal.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacultyRepository
        extends JpaRepository<Faculty, Long> {
}