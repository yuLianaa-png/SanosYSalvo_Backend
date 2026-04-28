package com.babygoat.pet_service.controlador;

import com.babygoat.pet_service.model.Pet;
import com.babygoat.pet_service.repository.petRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mascotas")
public class petController {
    @Autowired
    private petRepository repository;

    @PostMapping("/ingresar")
    public ResponseEntity<Pet> registrar(@RequestBody Pet pet) {
        return ResponseEntity.ok(repository.save(pet));
    }

    @GetMapping("/all")
    public List<Pet> listar() {
        return repository.findAll();
    }
}
