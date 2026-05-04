package com.babygoat.notification_service.Service;

import com.babygoat.notification_service.DTO.NotificacionDTO;
import com.babygoat.notification_service.Model.Notificacion;
import com.babygoat.notification_service.Repository.NotificacionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacionService {
    @Autowired
    private NotificacionRepository notificacionRepository;

    public void guardarNotificacion(NotificacionDTO dto) {
        ObjectMapper mapper = new ObjectMapper(); // ObjectMapper simple para evitar conflictos de módulos
        try {
            Notificacion notif = new Notificacion();
            notif.setUserId(dto.getUserId());
            notif.setMensaje(dto.getMensaje());
            // La fecha ya se setea por defecto en el modelo (private LocalDateTime fechaCreacion = LocalDateTime.now();)

            if (dto.getSugerencias() != null) {
                String jsonSugerencias = mapper.writeValueAsString(dto.getSugerencias());
                notif.setDataSugerencias(jsonSugerencias);
            }

            notificacionRepository.save(notif);
        } catch (Exception e) {
            e.printStackTrace(); // Esto te mostrará el error real en la consola
            throw new RuntimeException("Error en persistencia: " + e.getMessage());
        }
    }

    public List<Notificacion> obtenerNotificaciones(Long userId) {
        return notificacionRepository.findByUserIdOrderByFechaCreacionDesc(userId);
    }
}
