package com.studentportal.studentacademicportal.service;

import com.studentportal.studentacademicportal.entity.Notification;
import com.studentportal.studentacademicportal.entity.Student;
import com.studentportal.studentacademicportal.repository.NotificationRepository;
import com.studentportal.studentacademicportal.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final StudentRepository studentRepository;

    public NotificationService(
            NotificationRepository notificationRepository,
            StudentRepository studentRepository) {

        this.notificationRepository = notificationRepository;
        this.studentRepository = studentRepository;
    }

    public Notification createNotification(
            Long studentId,
            String title,
            String message,
            String type) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        Notification notification = new Notification();

        notification.setStudent(student);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        return notificationRepository.save(notification);
    }

    public List<Notification> getStudentNotifications(
            Long studentId) {

        return notificationRepository
                .findByStudentIdOrderByCreatedAtDesc(studentId);
    }

    public List<Notification> getUnreadNotifications(
            Long studentId) {

        return notificationRepository
                .findByStudentIdAndReadFalseOrderByCreatedAtDesc(
                        studentId);
    }

    public Notification markAsRead(Long notificationId) {

        Notification notification =
                notificationRepository.findById(notificationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification not found"));

        notification.setRead(true);

        return notificationRepository.save(notification);
    }

    public boolean deleteNotification(Long notificationId) {

        if (!notificationRepository.existsById(notificationId)) {
            return false;
        }

        notificationRepository.deleteById(notificationId);

        return true;
    }
}