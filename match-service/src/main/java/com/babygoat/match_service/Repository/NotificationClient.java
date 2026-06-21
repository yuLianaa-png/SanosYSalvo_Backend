package com.babygoat.match_service.Repository;

import com.babygoat.match_service.DTO.NotificationDTO;
import com.babygoat.match_service.Security.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", url = "http://sanos-salvos-notif:8080", configuration = FeignConfig.class)
public interface NotificationClient {
    @PostMapping("/api/notifications/send")
    void sendNotification(@RequestBody NotificationDTO dto);
}