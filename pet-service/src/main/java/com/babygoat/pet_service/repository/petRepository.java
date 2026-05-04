package com.babygoat.pet_service.repository;

import com.babygoat.pet_service.DTO.PetDTO;
import com.babygoat.pet_service.model.Pet;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface petRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByEstadoIgnoreCase(String estado);

    @Query("SELECT p FROM Pet p WHERE " +
            "LOWER(p.raza) = LOWER(:raza) AND " +
            "LOWER(p.color) = LOWER(:color) AND " +
            "LOWER(p.estado) = LOWER(:estado) AND " +
            "LOWER(p.ubicacion) = LOWER(:ubicacion)")
    List<PetDTO> buscarCoincidenciasManual(
            @Param("raza") String raza,
            @Param("color") String color,
            @Param("estado") String estado,
            @Param("ubicacion") String ubicacion
    );
}