//http

package com.babygoat.pet_service.Controller;

import com.babygoat.pet_service.DTO.PetDTO;
import com.babygoat.pet_service.Model.Pet;
import com.babygoat.pet_service.Service.PetService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class petController {

    private final PetService service;

    public petController(PetService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<Pet> register(@RequestBody Pet pet) {
        String identity = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return ResponseEntity.ok(service.registerPet(pet, identity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Pet petDetails) {
        try {
            return ResponseEntity.ok(service.updatePet(id, petDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.deletePet(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping
    public List<Pet> list() {
        return service.listAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/matches")
    public ResponseEntity<List<PetDTO>> searchMatches(
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String location) {

        List<PetDTO> results = service.searchByBreedAndColor(breed, color, location, status);

        if (results == null || results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(results);
    }

    @GetMapping("/status/{status}")
    public List<Pet> listByStatus(@PathVariable String status) {
        return service.searchByStatus(status);
    }
}