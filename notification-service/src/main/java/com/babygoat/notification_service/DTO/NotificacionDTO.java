package com.babygoat.notification_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionDTO {
    private Long userId;
    private String mensaje;
    private List<PetDTO> sugerencias;
}
