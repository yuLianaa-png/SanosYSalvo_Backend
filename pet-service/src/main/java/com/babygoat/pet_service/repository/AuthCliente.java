package com.babygoat.pet_service.repository;

import com.babygoat.pet_service.DTO.UsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", url = "${AUTH_SERVICE_URL:http://sanos-salvos-auth:8080}")
public interface AuthCliente {
    @GetMapping("/api/auth/usuario/{id}")
    UsuarioDTO obtenerUsuarioPorId(@PathVariable("id") Long id);
}
