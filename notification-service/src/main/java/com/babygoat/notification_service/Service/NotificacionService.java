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
        ObjectMapper mapper = new ObjectMapper();
        try {
            Notificacion notif = new Notificacion();
            notif.setUserId(dto.getUserId());
            notif.setMensaje(dto.getMensaje());

            if (dto.getSugerencias() != null) {
                String jsonSugerencias = mapper.writeValueAsString(dto.getSugerencias());
                notif.setDataSugerencias(jsonSugerencias);
            }

            notificacionRepository.save(notif);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error en persistencia: " + e.getMessage());
        }
    }

    public List<Notificacion> obtenerNotificaciones(Long userId) {
        return notificacionRepository.findByUserIdOrderByFechaCreacionDesc(userId);
    }
}
