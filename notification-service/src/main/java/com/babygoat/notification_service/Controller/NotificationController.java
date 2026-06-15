package com.babygoat.notification_service.Controller;

import com.babygoat.notification_service.DTO.NotificationDTO;
import com.babygoat.notification_service.Model.Notification;
import com.babygoat.notification_service.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<String> receiveNotification(@RequestBody NotificationDTO dto) {
        notificationService.saveNotification(dto);
        return ResponseEntity.ok("Notification processed");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getNotifications(userId));
    }
}
