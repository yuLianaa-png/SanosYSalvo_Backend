package com.babygoat.pet_service.controlador;

import com.babygoat.pet_service.model.Pet;
import com.babygoat.pet_service.repository.petRepository;
import com.babygoat.pet_service.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mascotas")
public class petController {
    @Autowired
    private PetService service;
    @Autowired
    private petRepository petRepository;

    @PostMapping
    // Agregamos @RequestParam para recibir el ID del usuario que registra
    public ResponseEntity<Pet> registrar(@RequestBody Pet pet, @RequestParam Long userId) {
        return ResponseEntity.ok(service.registrarMascota(pet, userId));
    }

    @GetMapping
    public List<Pet> listar() {
        return service.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> obtenerPorId(@PathVariable Long id) {
        // Usamos findById que es el estándar de JpaRepository
        return petRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
