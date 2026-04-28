package com.babygoat.match_service.Servicio;

import com.babygoat.match_service.DTO.petDTO;
import com.babygoat.match_service.repository.clienteMascota;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchService {
    @Autowired
    private clienteMascota Cliente;

    public List<petDTO> buscarMatches(String raza, String color) {
        List<petDTO> todas = Cliente.obtenerMascotas();
        return todas.stream()
                .filter(p -> p.getRaza().equalsIgnoreCase(raza) && p.getColor().equalsIgnoreCase(color))
                .collect(Collectors.toList());
    }

    public List<petDTO> buscarPorEstado(String estado) {
        // Obtenemos todas las mascotas del pet-service mediante Feign
        List<petDTO> todas = Cliente.obtenerMascotas();
        return todas.stream()
                .filter(p -> p.getEstado().equalsIgnoreCase(estado))
                .collect(Collectors.toList());
    }
}
