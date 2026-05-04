package com.babygoat.notification_service.Controller;

import com.babygoat.notification_service.DTO.NotificacionDTO;
import com.babygoat.notification_service.Model.Notificacion;
import com.babygoat.notification_service.Service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {
    @Autowired
    private NotificacionService notificacionService;

    @PostMapping("/enviar")
    public ResponseEntity<String> recibirNotificacion(@RequestBody NotificacionDTO dto) {
        notificacionService.guardarNotificacion(dto);
        return ResponseEntity.ok("Notificación procesada internamente");
    }

    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<Notificacion>> obtenerPorUsuario(@PathVariable Long userId) {
        return ResponseEntity.ok(notificacionService.obtenerNotificaciones(userId));
    }
}
