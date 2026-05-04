package com.babygoat.match_service.controlador;

import com.babygoat.match_service.DTO.MatchDTO;
import com.babygoat.match_service.DTO.MatchResponseDTO;
import com.babygoat.match_service.Servicio.MatchService;
import com.babygoat.match_service.model.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class matchController {

    @Autowired
    private MatchService matchService;

    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<Match>> listarPorUsuario(@PathVariable Long userId) {
        List<Match> matches = matchService.obtenerMatchesPorUsuario(userId);
        return ResponseEntity.ok(matches);
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody MatchDTO request) {
        try {
            MatchResponseDTO respuestaCompleta = matchService.crearMatchValidado(request);

            return new ResponseEntity<>(respuestaCompleta, HttpStatus.CREATED);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en el procesamiento interno del match: " + e.getMessage());
        }
    }

    // Obtener todos los matches (Para una vista general de administración)
    @GetMapping
    public ResponseEntity<List<Match>> listarTodos() {
        return ResponseEntity.ok(matchService.listarTodos());
    }

}
