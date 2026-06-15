//http

package com.babygoat.pet_service.Controller;


import com.babygoat.pet_service.DTO.PetDTO;
import com.babygoat.pet_service.Model.Pet;
import com.babygoat.pet_service.Repository.petRepository;
import com.babygoat.pet_service.Service.PetService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class petController {
    @Autowired
    private PetService service;
    @Autowired
    private petRepository petRepository;

    public petController() {
    }

    public petController(PetService service, petRepository petRepository) {
        this.service = service;
        this.petRepository = petRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<Pet> register(@RequestBody Pet pet) {
        String identity = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();

        return ResponseEntity.ok(service.registerPet(pet, identity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Pet petDetails) {
        try {
            Pet updated = service.updatePet(id, petDetails);
            return ResponseEntity.ok(updated);
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
        return petRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/matches")
    public ResponseEntity<List<PetDTO>> searchMatches(
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String location) {

        breed = (breed != null && !breed.trim().isEmpty()) ? breed.trim() : null;
        color = (color != null && !color.trim().isEmpty()) ? color.trim() : null;
        status = (status != null && !status.trim().isEmpty()) ? status.trim() : null;
        location = (location != null && !location.trim().isEmpty()) ? location.trim() : null;

        List<PetDTO> results = service.searchByBreedAndColor(breed, color, location, status);

        if (results == null || results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(results);
    }

    // Show pets by their status
    @GetMapping("/status/{status}")
    public List<Pet> listByStatus(@PathVariable("status") String status) {
        return service.searchByStatus(status);
    }
}
