package com.babygoat.match_service.controlador;

import com.babygoat.match_service.DTO.MatchDTO;
import com.babygoat.match_service.DTO.MatchResponseDTO;
import com.babygoat.match_service.Service.MatchService;
import com.babygoat.match_service.model.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Match>> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(matchService.getMatchesByUserId(userId));
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody MatchDTO request) {
        try {
            MatchResponseDTO response = matchService.createValidatedMatch(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal match processing error: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Match>> getAll() {
        return ResponseEntity.ok(matchService.getAllMatches());
    }
}
