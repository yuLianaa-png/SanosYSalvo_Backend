package com.babygoat.match_service.repository;

import com.babygoat.match_service.DTO.petDTO;
import com.babygoat.match_service.security.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "pet-service", url = "http://sanos-salvos-petserv:8080", configuration = FeignConfig.class)
public interface clienteMascota {
    @GetMapping("/api/mascotas/{id}")
    petDTO obtenerMascotaPorId(@PathVariable("id") Long id);

    @GetMapping("/api/mascotas/buscar/match")
    List<petDTO> buscarPorFiltros(@RequestParam("raza") String raza, @RequestParam("color") String color, @RequestParam("ubicacion") String ubicacion, @RequestParam("estado") String estado);
}
