package com.babygoat.match_service.controlador;

import com.babygoat.match_service.DTO.petDTO;
import com.babygoat.match_service.Servicio.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class matchController {

    @Autowired
    private MatchService matchService;

    @GetMapping("/estado/{estado}")
    public List<petDTO> listarPorEstado(@RequestParam String estado) {
        return matchService.buscarPorEstado(estado);
    }

    @GetMapping("/buscar")
    public List<petDTO> buscarCoincidencias(
            @RequestParam String raza,
            @RequestParam String color) {

        return matchService.buscarMatches(raza, color);
    }
}
