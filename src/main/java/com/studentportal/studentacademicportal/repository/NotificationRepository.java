package com.studentportal.studentacademicportal.repository;

import com.studentportal.studentacademicportal.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification> findByStudentIdOrderByCreatedAtDesc(Long studentId);

    List<Notification> findByStudentIdAndReadFalseOrderByCreatedAtDesc(
            Long studentId);
}