package com.studentportal.studentacademicportal.repository;

import com.studentportal.studentacademicportal.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByStudentId(Long studentId);

    List<Attendance> findBySubjectId(Long subjectId);

    boolean existsByStudentIdAndSubjectIdAndAttendanceDate(
            Long studentId,
            Long subjectId,
            LocalDate attendanceDate
    );

    long countByStudentIdAndSubjectId(Long studentId, Long subjectId);

    long countByStudentIdAndSubjectIdAndPresentTrue(
            Long studentId,
            Long subjectId
    );
}