package com.babygoat.pet_service.Repository;

import com.babygoat.pet_service.DTO.PetDTO;
import com.babygoat.pet_service.Model.Pet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface petRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByStatusIgnoreCase(String status);

    @Query("""
        SELECT new com.babygoat.pet_service.DTO.PetDTO(
            p.id,
            p.userId,
            p.name,
            p.breed,
            p.color,
            p.status,
            p.location
        )
        FROM Pet p
        WHERE (:breed IS NULL OR :breed = '' OR LOWER(p.breed) = LOWER(:breed))
          AND (:color IS NULL OR :color = '' OR LOWER(p.color) = LOWER(:color))
          AND (:location IS NULL OR :location = '' OR LOWER(p.location) = LOWER(:location))
          AND (:status IS NULL OR :status = '' OR LOWER(p.status) = LOWER(:status))
    """)
    List<PetDTO> searchMatchesManual(
            @Param("breed") String breed,
            @Param("color") String color,
            @Param("location") String location,
            @Param("status") String status
    );
}