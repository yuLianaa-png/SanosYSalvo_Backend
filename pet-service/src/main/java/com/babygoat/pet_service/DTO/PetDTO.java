package com.babygoat.pet_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetDTO {
    private Long id;
    private Long usuarioId; // Asegúrate de que coincida con el nombre en Match-Service
    private String nombre;
    private String raza;
    private String color;
    private String estado;
    private String ubicacion;
}
