package com.babygoat.pet_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "mascotas")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private Long usuarioId;

    private String nombre;
    private String raza;
    private String color;
    private String tamano;
    private String estado; // "PERDIDA" o "ENCONTRADA"
    private String ubicacion;
    private String contactoTutor;
    private LocalDateTime fechaReporte = LocalDateTime.now();

}

