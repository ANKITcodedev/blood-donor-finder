package com.ankit.BloodDonorFinder.controller;

import com.ankit.BloodDonorFinder.model.Notification;
import com.ankit.BloodDonorFinder.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
@Tag(name = "Notifications",
        description = "Manage in-app notifications")
public class NotificationController {

    private final NotificationService notificationService;

    // get all user notifications
    @Operation(
            summary = "Get all notifications",
            description = "Get all notifications for a user " +
                    "including read and unread."
    )
    @GetMapping("/{userId}")
    public ResponseEntity<List<Notification>> getAllNotification(@PathVariable Long userId) {

        return ResponseEntity.ok(notificationService.getMyNotification(userId));
    }

    // get unread notifications only
    @Operation(
            summary = "Get unread notifications",
            description = "Get only unread notifications for a user."
    )
    @GetMapping("/unread/{userId}")
    public ResponseEntity<List<Notification>> getUnreadNotification(@PathVariable Long userId) {

        return ResponseEntity.ok(notificationService.getUnreadNotifications(userId));
    }

    // get unread count
    @Operation(
            summary = "Get unread count",
            description = "Get count of unread notifications. " +
                    "Used to show badge number on app icon."
    )
    @GetMapping("/count/{userId}")
    public ResponseEntity<Long> getUnreadCount(@PathVariable Long userId) {

        return ResponseEntity.ok(notificationService.getUnreadCount(userId));
    }

    // mark single notification as read
    @Operation(
            summary = "Mark one as read",
            description = "Mark a single notification as read " +
                    "when user clicks on it."
    )
    @PutMapping("/read/{notificationId}")
    public ResponseEntity<String> markAsRead(@PathVariable Long notificationId) {

        return ResponseEntity.ok(notificationService.markAsRead(notificationId));
    }

    // mark all notification as read
    @Operation(
            summary = "Mark all as read",
            description = "Mark all notifications as read " +
                    "when user clicks 'Mark all as read'."
    )
    @PutMapping("/read-all/{userId}")
    public ResponseEntity<String> markAllAsRead(@PathVariable Long userId) {

        return ResponseEntity.ok(notificationService.markAllAsRead(userId));
    }
}
