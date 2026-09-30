package com.studentportal.studentacademicportal.controller;

import com.studentportal.studentacademicportal.dto.NotificationResponse;
import com.studentportal.studentacademicportal.entity.Notification;
import com.studentportal.studentacademicportal.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService) {

        this.notificationService = notificationService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY')")
    public ResponseEntity<NotificationResponse> createNotification(
            @RequestParam Long studentId,
            @RequestParam String title,
            @RequestParam String message,
            @RequestParam String type) {

        Notification notification =
                notificationService.createNotification(
                        studentId,
                        title,
                        message,
                        type);

        return ResponseEntity.ok(toResponse(notification));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<NotificationResponse>>
    getStudentNotifications(
            @PathVariable Long studentId) {

        List<NotificationResponse> notifications =
                notificationService
                        .getStudentNotifications(studentId)
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/student/{studentId}/unread")
    public ResponseEntity<List<NotificationResponse>>
    getUnreadNotifications(
            @PathVariable Long studentId) {

        List<NotificationResponse> notifications =
                notificationService
                        .getUnreadNotifications(studentId)
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationResponse> markAsRead(
            @PathVariable Long id) {

        Notification notification =
                notificationService.markAsRead(id);

        return ResponseEntity.ok(toResponse(notification));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteNotification(
            @PathVariable Long id) {

        boolean deleted =
                notificationService.deleteNotification(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    private NotificationResponse toResponse(
            Notification notification) {

        return new NotificationResponse(
                notification.getId(),
                notification.getStudent().getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getType(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}