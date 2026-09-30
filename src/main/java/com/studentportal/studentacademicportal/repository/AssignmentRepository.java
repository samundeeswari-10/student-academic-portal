package com.studentportal.studentacademicportal.repository;

import com.studentportal.studentacademicportal.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentRepository
        extends JpaRepository<Assignment, Long> {

    List<Assignment> findBySubjectId(Long subjectId);
}