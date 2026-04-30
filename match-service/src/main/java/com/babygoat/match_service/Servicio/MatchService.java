package com.babygoat.match_service.Servicio;

import com.babygoat.match_service.DTO.MatchDTO;
import com.babygoat.match_service.DTO.petDTO;
import com.babygoat.match_service.model.Match;
import com.babygoat.match_service.repository.clienteMascota;
import com.babygoat.match_service.repository.matchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchService {
    @Autowired
    private clienteMascota Cliente;
    @Autowired
    private matchRepository matchRepository;

    //Metodo para buscar mascotas por raza y color
    public List<petDTO> buscarMatches(String raza, String color) {
        List<petDTO> todas = Cliente.obtenerMascotas();
        return todas.stream()
                .filter(p -> p.getRaza() != null && p.getColor() != null)
                .filter(p -> p.getRaza().equalsIgnoreCase(raza) && p.getColor().equalsIgnoreCase(color))
                .collect(Collectors.toList());
    }

    // Metodo para mostrar mascotas según su estado
    public List<petDTO> buscarPorEstado(String estado) {
        // Obtenemos todas las mascotas del pet-service mediante Feign
        List<petDTO> todas = Cliente.obtenerMascotas();
        return todas.stream()
                .filter(p -> p.getEstado().equalsIgnoreCase(estado))
                .collect(Collectors.toList());
    }

    //Metodo para crear el match entre usuario y mascota
    public Match crearMatchValidado(MatchDTO request) {
        try {
            //petDTO mascota = Cliente.obtenerMascotaPorId(request.getPetId());

            Match nuevoMatch = new Match();
            nuevoMatch.setPetId(request.getPetId());
            nuevoMatch.setUserId(request.getUserId());

            return matchRepository.save(nuevoMatch);

        } catch (Exception e) {
            // Si el Pet Service devuelve 404 o está caído, lanzamos error
            throw new RuntimeException("No se pudo realizar el match: Mascota no encontrada o servicio no disponible");
        }
    }

    // Metodo que devuelve las mascotas que estan vinculadas a un usuario
    public List<Match> obtenerMatchesPorUsuario(Long userId) {
        return matchRepository.findByUserId(userId);
    }
}
