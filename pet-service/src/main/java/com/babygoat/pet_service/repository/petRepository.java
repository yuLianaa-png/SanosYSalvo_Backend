package com.babygoat.pet_service.repository;

import com.babygoat.pet_service.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface petRepository extends JpaRepository<Pet, Long> {
}

