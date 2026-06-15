package com.babygoat.match_service.DTO;

import com.babygoat.match_service.Model.Match;

import java.util.List;

public class MatchResponseDTO {
    private Match createdMatch;
    private List<PetDTO> matches;

    public MatchResponseDTO() {
    }

    public MatchResponseDTO(Match createdMatch, List<PetDTO> matches) {
        this.createdMatch = createdMatch;
        this.matches = matches;
    }

    public Match getCreatedMatch() {
        return createdMatch;
    }

    public void setCreatedMatch(Match createdMatch) {
        this.createdMatch = createdMatch;
    }

    public List<PetDTO> getMatches() {
        return matches;
    }

    public void setMatches(List<PetDTO> matches) {
        this.matches = matches;
    }
}
