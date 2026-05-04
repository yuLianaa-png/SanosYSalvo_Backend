package com.babygoat.match_service.repository;

import com.babygoat.match_service.DTO.NotificacionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", url = "http://sanos-salvos-notif:8080")
public interface NotificacionUsuario {
    @PostMapping("/api/notificaciones/enviar")
    void enviarNotificacion(@RequestBody NotificacionDTO dto);
}
