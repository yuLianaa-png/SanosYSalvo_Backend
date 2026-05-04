package com.babygoat.match_service.DTO;

import com.babygoat.match_service.model.Match;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchResponseDTO {
    private Match matchCreado;
    private List<petDTO> coincidencias;
}
