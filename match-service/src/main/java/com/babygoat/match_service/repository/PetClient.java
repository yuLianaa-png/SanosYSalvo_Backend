package com.babygoat.match_service.Repository;

import com.babygoat.match_service.DTO.PetDTO;
import com.babygoat.match_service.Recurity.FeignConfig;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "pet-service", url = "http://sanos-salvos-petserv:8080", configuration = FeignConfig.class)
public interface PetClient {
    @GetMapping("/api/mascotas/{id}")
    PetDTO getPetById(@PathVariable("id") Long id);

    @GetMapping("/api/mascotas/buscar/match")
    List<PetDTO> searchByFilters(@RequestParam("raza") String breed, @RequestParam("color") String color, @RequestParam("ubicacion") String location, @RequestParam("estado") String status);
}