package com.babygoat.match_service.Servicio;

import com.babygoat.match_service.DTO.MatchDTO;
import com.babygoat.match_service.model.Match;
import com.babygoat.match_service.repository.matchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService {
    @Autowired
    private matchRepository matchRepository;

    //Crear el match entre usuario y mascota
    public Match crearMatchValidado(MatchDTO request) {
        try {
            Match nuevoMatch = new Match();
            nuevoMatch.setPetId(request.getPetId());
            nuevoMatch.setUserId(request.getUserId());

            return matchRepository.save(nuevoMatch);

        } catch (Exception e) {
            // Si el Pet Service devuelve 404 o está caído, lanzamos error
            throw new RuntimeException("No se pudo realizar el match: Mascota no encontrada o servicio no disponible");
        }
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
