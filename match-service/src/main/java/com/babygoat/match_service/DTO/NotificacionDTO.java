package com.babygoat.match_service.DTO;

import lombok.Data;

import java.util.List;

@Data
public class NotificacionDTO {
    private Long userId;
    private String mensaje;
    private List<petDTO> sugerencias;
}
