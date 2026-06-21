package com.babygoat.match_service.Service;

import com.babygoat.match_service.DTO.MatchDTO;
import com.babygoat.match_service.DTO.MatchResponseDTO;
import com.babygoat.match_service.DTO.NotificationDTO;
import com.babygoat.match_service.DTO.petDTO;
import com.babygoat.match_service.Model.Match;
import com.babygoat.match_service.Repository.matchRepository;
import com.babygoat.match_service.Repository.NotificationClient;
import com.babygoat.match_service.Repository.PetClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList; 

@Service
public class MatchService {
    @Autowired
    private matchRepository matchRepository;

    @Autowired
    private NotificationClient notificationClient;

    @Autowired
    private PetClient petClient;

    public MatchResponseDTO createValidatedMatch(MatchDTO request) {
        Match newMatch = new Match();
        newMatch.setPetId(request.getPetId());
        newMatch.setUserId(request.getUserId());

        Match savedMatch = matchRepository.save(newMatch);

        // Estados ingleses para mantener consistencia con el pet-service
        String searchStatus = request.getStatus().equalsIgnoreCase("LOST") ? "FOUND" : "LOST";

        List<petDTO> matches = petClient.searchByFilters(
                request.getBreed(),
                request.getColor(),
                request.getLocation(),
                searchStatus
        );

        // validar si matches es nulo o vacío antes de enviar notificaciones
        if (matches != null && !matches.isEmpty()) {
            NotificationDTO currentUserNotification = new NotificationDTO();
            currentUserNotification.setUserId(request.getUserId());
            currentUserNotification.setMessage("We found " + matches.size() + " possible matches for your pet!");
            currentUserNotification.setSuggestions(matches);
            notificationClient.sendNotification(currentUserNotification);

            for (petDTO match : matches) {
                petDTO currentPet = new petDTO();
                currentPet.setId(request.getPetId());
                currentPet.setBreed(request.getBreed());
                currentPet.setColor(request.getColor());
                currentPet.setLocation(request.getLocation());
                currentPet.setStatus(request.getStatus());
                currentPet.setUserId(request.getUserId());

                NotificationDTO matchNotification = new NotificationDTO();
                matchNotification.setUserId(match.getUserId());
                matchNotification.setMessage("Someone posted a pet that matches yours (" + request.getBreed() + ").");
                matchNotification.setSuggestions(Collections.singletonList(currentPet));

                notificationClient.sendNotification(matchNotification);
            }
        } else {
            // vacío, lo inicializamos como una lista limpia para que el ResponseDTO no devuelva null
            matches = new ArrayList<>();
        }

        return new MatchResponseDTO(savedMatch, matches);
    }

    public List<Match> getMatchesByUserId(Long userId) {
        return matchRepository.findByUserId(userId);
    }

    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    public void deleteMatch(Long id) {
        if (!matchRepository.existsById(id)) {
            throw new RuntimeException("Match not found");
        }
        matchRepository.deleteById(id);
    }
}