package com.babygoat.match_service.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class petDTO {
    private String nombre;
    private String raza;
    private String color;
    private String tamano;
    private String estado;
    private String ubicacion;
    private String contactoTutor;
    private LocalDateTime fechaReporte = LocalDateTime.now();
}
