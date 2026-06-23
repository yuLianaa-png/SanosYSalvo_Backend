package com.babygoat.match_service.Repository;

import com.babygoat.match_service.DTO.petDTO;
import com.babygoat.match_service.Security.FeignConfig;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "pet-service", url = "${PET_SERVICE_URL:http://pet-service:8080}", configuration = FeignConfig.class)
public interface PetClient {
    @GetMapping("/api/v1/mascotas/{id}")
    petDTO getPetById(@PathVariable("id") Long id);

    @GetMapping("/api/v1/mascotas/search/matches")
    List<petDTO> searchByFilters(
            @RequestParam("breed") String breed,
            @RequestParam("color") String color,
            @RequestParam("status") String status,
            @RequestParam("location") String location);
}