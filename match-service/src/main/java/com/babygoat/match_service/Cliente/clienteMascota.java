package com.babygoat.match_service.Cliente;

import com.babygoat.match_service.DTO.petDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "pet-service", url = "http://pet-service:8080/api/mascotas")
public interface clienteMascota {
    @GetMapping
    List<petDTO> obtenerMascotas();
}
