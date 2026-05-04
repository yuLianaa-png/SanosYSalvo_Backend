package com.babygoat.notification_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetDTO {
    private Long usuarioId;
    private Long id;
    private String raza;
    private String color;
    private String estado; // "PERDIDA" o "ENCONTRADA"
    private String ubicacion;
}
