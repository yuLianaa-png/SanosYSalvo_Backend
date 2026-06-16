//http

package com.babygoat.pet_service.Controller;

import com.babygoat.pet_service.DTO.PetDTO;
import com.babygoat.pet_service.Model.Pet;
import com.babygoat.pet_service.Service.PetService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mascotas")
public class petController {

    private final PetService service;

    public petController(PetService service) {
        this.service = service;
    }

    //  registrar nueva mascota
    @PostMapping
    public ResponseEntity<Pet> register(@RequestBody Pet pet) {

        String identity = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Pet created = service.registerPet(pet, identity);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // actualizar mascota por id
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Pet petDetails) {
        try {
            return ResponseEntity.ok(service.updatePet(id, petDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // borrar mascota por id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.deletePet(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // obtener todas las mascotas
    @GetMapping
    public List<Pet> list() {
        return service.listAll();
    }

    // obtener por id
    @GetMapping("/{id}")
    public ResponseEntity<Pet> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // buscar coincidencias (filtros opcionales)
    @GetMapping("/search/matches")
    public ResponseEntity<List<PetDTO>> searchMatches(
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String location) {

        List<PetDTO> results =
                service.searchByBreedAndColor(breed, color, location, status);

        if (results == null || results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(results);
    }

    // buscar por estado (perdida/encontrada)
    @GetMapping("/status/{status}")
    public List<Pet> listByStatus(@PathVariable String status) {
        return service.searchByStatus(status);
    }
}