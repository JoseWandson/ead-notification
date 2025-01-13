package com.ead.notification.controllers;

import com.ead.notification.dtos.NotificationDto;
import com.ead.notification.models.NotificationModel;
import com.ead.notification.services.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users/{userId}/notifications")
public class UserNotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<Page<NotificationModel>> getAllNotificationsByUser(@PathVariable UUID userId, @PageableDefault(sort = "notificationId") Pageable pageable) {
        return ResponseEntity.ok(notificationService.findAllNotificationByUser(userId, pageable));
    }

    @PutMapping("/{notificationId}")
    public ResponseEntity<Object> updateNotification(@PathVariable UUID userId, @PathVariable UUID notificationId, @RequestBody @Valid NotificationDto notificationDto) {
        Optional<NotificationModel> notificationModelOptional = notificationService.findByNotificationIdAndUserId(notificationId, userId);
        if (notificationModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notification not found!");
        }
        NotificationModel notificationModel = notificationModelOptional.get();
        notificationModel.setNotificationStatus(notificationDto.getNotificationStatus());

        notificationService.saveNotification(notificationModel);

        return ResponseEntity.ok(notificationModel);
    }
}
