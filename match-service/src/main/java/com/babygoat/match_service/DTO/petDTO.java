package com.babygoat.match_service.DTO;

import lombok.Data;

@Data
public class petDTO {
    private Long id;
    private String nombre;
    private String raza;
    private String color;
    private String tamano;
    private String estado;
}
