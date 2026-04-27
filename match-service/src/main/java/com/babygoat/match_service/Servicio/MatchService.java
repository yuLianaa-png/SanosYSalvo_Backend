package com.babygoat.match_service.Servicio;

import com.babygoat.match_service.Cliente.clienteMascota;
import com.babygoat.match_service.DTO.petDTO;
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
        // 1. Obtenemos todas las mascotas del pet-service mediante Feign
        List<petDTO> todas = Cliente.obtenerMascotas();

        // 2. Filtramos solo por el estado solicitado (ignorando mayúsculas)
        return todas.stream()
                .filter(p -> p.getEstado().equalsIgnoreCase(estado))
                .collect(Collectors.toList());
    }
}
