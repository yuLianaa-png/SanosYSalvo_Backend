package com.babygoat.match_service.controlador;

import com.babygoat.match_service.DTO.MatchDTO;
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
            Match guardado = matchService.crearMatchValidado(request);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (Exception e) {
            // Esto te dirá en los logs de Docker por qué falló el guardado
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Obtener todos los matches (Para una vista general de administración)
    @GetMapping
    public ResponseEntity<List<Match>> listarTodos() {
        return ResponseEntity.ok(matchService.listarTodos());
    }

    // Eliminar un match (Cuando la mascota ya fue entregada o fue un error)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMatch(@PathVariable Long id) {
        matchService.eliminarMatch(id);
        return ResponseEntity.noContent().build();
    }
}
