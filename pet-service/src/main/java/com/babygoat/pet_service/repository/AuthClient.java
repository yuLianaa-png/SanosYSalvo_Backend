package com.babygoat.pet_service.Repository;

import com.babygoat.pet_service.DTO.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", url = "http://sanos-salvos-auth:8080")
public interface AuthClient {
    @GetMapping("/api/auth/search/{username}")
    UserDTO getUserByUsername(@PathVariable("username") String username);
}
