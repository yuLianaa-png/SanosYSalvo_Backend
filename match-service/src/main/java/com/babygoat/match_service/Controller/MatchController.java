package com.babygoat.match_service.Controller;

import com.babygoat.match_service.DTO.MatchDTO;
import com.babygoat.match_service.Model.Match;
import com.babygoat.match_service.Service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> create(@RequestHeader(value = "X-Internal-Secret", required = false) String internalSecret,
                                    @RequestBody MatchDTO request) {
        String expectedSecret = System.getenv("INTERNAL_SERVICE_SECRET");

        if (expectedSecret == null || !expectedSecret.equals(internalSecret)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid internal secret");
        }

        return ResponseEntity.ok(matchService.createValidatedMatch(request));

        /*try {
            MatchResponseDTO response = matchService.createValidatedMatch(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal match processing error: " + e.getMessage());
        }*/
    }

    @GetMapping
    public ResponseEntity<List<Match>> getAll() {
        return ResponseEntity.ok(matchService.getAllMatches());
    }
}
