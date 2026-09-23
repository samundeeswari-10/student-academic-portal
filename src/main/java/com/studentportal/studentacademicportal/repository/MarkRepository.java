package com.studentportal.studentacademicportal.repository;

import com.studentportal.studentacademicportal.entity.Mark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MarkRepository extends JpaRepository<Mark, Long> {

    Optional<Mark> findByStudentIdAndSubjectId(
            Long studentId,
            Long subjectId
    );

    List<Mark> findByStudentId(Long studentId);

    List<Mark> findBySubjectId(Long subjectId);

    boolean existsByStudentIdAndSubjectId(
            Long studentId,
            Long subjectId
    );
}