package com.babygoat.match_service.repository;

import com.babygoat.match_service.DTO.petDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "pet-service", url = "${PET_SERVICE_URL}")

public interface clienteMascota {
    @GetMapping("/api/mascotas")
    List<petDTO> obtenerMascotas();

    @GetMapping("/api/mascotas/{id}")
    petDTO obtenerMascotaPorId(@PathVariable("id") Long id);
}
