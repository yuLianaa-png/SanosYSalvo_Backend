package com.babygoat.notification_service.Controller;

import com.babygoat.notification_service.DTO.NotificationDTO;
import com.babygoat.notification_service.DTO.UserDTO;
import com.babygoat.notification_service.Model.Notification;
import com.babygoat.notification_service.Repository.AuthClient;
import com.babygoat.notification_service.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AuthClient authClient;

    @PostMapping("/send")
    public ResponseEntity<String> receiveNotification(@RequestBody NotificationDTO dto) {
        notificationService.saveNotification(dto);
        return ResponseEntity.ok("Notification processed");
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyNotifications() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        UserDTO user = authClient.getUserByUsername(username);

        if (user == null || user.getId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        List<Notification> notifications =
                notificationService.getNotifications(user.getId());

        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/internal/match")
    public ResponseEntity<?> createInternalMatchNotification(
            @RequestHeader(value = "X-Internal-Secret", required = false) String internalSecret,
            @RequestBody NotificationDTO notificationDTO
    ) {
        String expectedSecret = System.getenv("INTERNAL_SERVICE_SECRET");

        if (expectedSecret == null || !expectedSecret.equals(internalSecret)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid internal secret");
        }

        notificationService.saveNotification(notificationDTO);

        return ResponseEntity.ok("Notification created");
    }
}
