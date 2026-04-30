package com.babygoat.match_service.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
@Data

public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Guardamos solo el ID que viene del Pet Service
    @Column(name = "petid", nullable = false)
    private Long petId;

    // Guardamos el ID del usuario que viene del Auth Service
    @Column(name = "userid", nullable = false)
    private Long userId;

    private LocalDateTime fechaMatch;

    @PrePersist
    protected void onCreate() {
        fechaMatch = LocalDateTime.now();
    }
}
