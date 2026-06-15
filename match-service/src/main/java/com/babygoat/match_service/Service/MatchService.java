package com.babygoat.match_service.Service;

import com.babygoat.match_service.DTO.MatchDTO;
import com.babygoat.match_service.DTO.MatchResponseDTO;
import com.babygoat.match_service.DTO.NotificationDTO;
import com.babygoat.match_service.DTO.PetDTO;
import com.babygoat.match_service.Model.Match;
import com.babygoat.match_service.Repository.MatchRepository;
import com.babygoat.match_service.Repository.NotificationClient;
import com.babygoat.match_service.Repository.PetClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class MatchService {
    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private NotificationClient notificationClient;

    @Autowired
    private PetClient petClient;

    public MatchResponseDTO createValidatedMatch(MatchDTO request) {
        Match newMatch = new Match();
        newMatch.setPetId(request.getPetId());
        newMatch.setUserId(request.getUserId());

        Match savedMatch = matchRepository.save(newMatch);

        String searchStatus = request.getStatus().equalsIgnoreCase("PERDIDA") ? "ENCONTRADA" : "PERDIDA";

        List<PetDTO> matches = petClient.searchByFilters(
                request.getBreed(),
                request.getColor(),
                request.getLocation(),
                searchStatus
        );

        if (!matches.isEmpty()) {
            NotificationDTO currentUserNotification = new NotificationDTO();
            currentUserNotification.setUserId(request.getUserId());
            currentUserNotification.setMessage("We found " + matches.size() + " possible matches for your pet!");
            currentUserNotification.setSuggestions(matches);
            notificationClient.sendNotification(currentUserNotification);

            for (PetDTO match : matches) {
                PetDTO currentPet = new PetDTO();
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
