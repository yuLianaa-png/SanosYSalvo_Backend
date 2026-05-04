package com.babygoat.match_service.Servicio;

import com.babygoat.match_service.DTO.MatchDTO;
import com.babygoat.match_service.DTO.MatchResponseDTO;
import com.babygoat.match_service.DTO.NotificacionDTO;
import com.babygoat.match_service.DTO.petDTO;
import com.babygoat.match_service.model.Match;
import com.babygoat.match_service.repository.NotificacionUsuario;
import com.babygoat.match_service.repository.clienteMascota;
import com.babygoat.match_service.repository.matchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class MatchService {
    @Autowired
    private matchRepository matchRepository;

    @Autowired
    private NotificacionUsuario notificacionUsuario;

    @Autowired
    private clienteMascota petCliente;

    //Crear el match
    public MatchResponseDTO crearMatchValidado(MatchDTO request) {
        Match nuevoMatch = new Match();
        nuevoMatch.setPetId(request.getPetId());
        nuevoMatch.setUserId(request.getUserId());

        System.out.println("Intentando guardar Match con PetID: " + request.getPetId() + " y UserID: " + request.getUserId());
        Match matchGuardado = matchRepository.save(nuevoMatch);

        String estadoBusqueda = request.getEstado().equalsIgnoreCase("PERDIDA") ? "ENCONTRADA" : "PERDIDA";

        List<petDTO> coincidencias = petCliente.buscarPorFiltros(
                request.getRaza(),
                request.getColor(),
                request.getUbicacion(),
                estadoBusqueda
        );

        if (!coincidencias.isEmpty()) {
            // A. Notificar al usuario que acaba de subir la publicación
            NotificacionDTO notifUsuarioActual = new NotificacionDTO();
            notifUsuarioActual.setUserId(request.getUserId());
            notifUsuarioActual.setMensaje("¡Hemos encontrado " + coincidencias.size() + " posibles coincidencias para tu mascota!");
            notifUsuarioActual.setSugerencias(coincidencias);
            notificacionUsuario.enviarNotificacion(notifUsuarioActual);

            // B. Notificar a cada dueño de las mascotas encontradas
            for (petDTO coincidencia : coincidencias) {
                petDTO mascotaActualInfo = new petDTO();
                mascotaActualInfo.setId(request.getPetId());
                mascotaActualInfo.setRaza(request.getRaza());
                mascotaActualInfo.setColor(request.getColor());
                mascotaActualInfo.setUbicacion(request.getUbicacion());
                mascotaActualInfo.setEstado(request.getEstado());
                mascotaActualInfo.setUsuarioId(request.getUserId());

                NotificacionDTO notifMatch = new NotificacionDTO();
                notifMatch.setUserId(coincidencia.getUsuarioId());
                notifMatch.setMensaje("Alguien ha publicado una mascota que coincide con la tuya (" + request.getRaza() + ").");
                notifMatch.setSugerencias(Collections.singletonList(mascotaActualInfo));

                // Enviamos la notificación
                notificacionUsuario.enviarNotificacion(notifMatch);
            }
        }

        return new MatchResponseDTO(matchGuardado, coincidencias);
    }

    // Devuelve las mascotas que estan vinculadas a un usuario
    public List<Match> obtenerMatchesPorUsuario(Long userId) {
        return matchRepository.findByUserId(userId);
    }

    //Listar matches
    public List<Match> listarTodos() {
        return matchRepository.findAll();
    }

    //Eliminar match
    public void eliminarMatch(Long id) {
        if (!matchRepository.existsById(id)) {
            throw new RuntimeException("Match no encontrado");
        }
        matchRepository.deleteById(id);
    }
}
