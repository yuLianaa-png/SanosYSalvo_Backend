package com.babygoat.pet_service.repository;

import com.babygoat.pet_service.DTO.PetDTO;
import com.babygoat.pet_service.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface petRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByEstadoIgnoreCase(String estado);

    @Query("SELECT new com.babygoat.pet_service.DTO.PetDTO(p.id, p.usuarioId, p.nombre, p.raza, p.color, p.estado, p.ubicacion) FROM Pet p WHERE " +
            "(:raza IS NULL OR LOWER(p.raza) = LOWER(:raza)) AND " +
            "(:color IS NULL OR LOWER(p.color) = LOWER(:color)) AND " +
            "(:ubicacion IS NULL OR LOWER(p.ubicacion) = LOWER(:ubicacion)) AND " +
            "(:estado IS NULL OR LOWER(p.estado) = LOWER(:estado))")
    List<PetDTO> buscarCoincidenciasManual(
            @Param("raza") String raza,
            @Param("color") String color,
            @Param("ubicacion") String ubicacion,
            @Param("estado") String estado
    );
}