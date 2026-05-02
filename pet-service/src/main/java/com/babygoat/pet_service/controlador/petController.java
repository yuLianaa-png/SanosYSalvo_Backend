//http

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

    //Registra mascota
    @PostMapping
    // Agregamos @RequestParam para recibir el ID del usuario que registra
    public ResponseEntity<Pet> registrar(@RequestBody Pet pet, @RequestParam Long userId) {
        return ResponseEntity.ok(service.registrarMascota(pet, userId));
    }

    //Actualiza detalle de mascota
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Pet petDetalles) {
        try {
            Pet actualizado = service.actualizarMascota(id, petDetalles);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    //Elimina mascota
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            service.eliminarMascota(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    //Lista todas las mascotas que hay en el sistema
    @GetMapping
    public List<Pet> listar() {
        return service.listarTodas();
    }

    //Obtener mascota por id
    @GetMapping("/{id}")
    public ResponseEntity<Pet> obtenerPorId(@PathVariable Long id) {
        // Usamos findById que es el estándar de JpaRepository
        return petRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //buscar mascotas por raza y color
    @GetMapping("/buscar")
    public List<Pet> buscarCoincidencias(
            @RequestParam String raza,
            @RequestParam String color) {

        return service.buscarPorRazaYColor(raza, color);
    }

    //mostrar mascotas según su estado
    @GetMapping("/estado/{estado}")
    public List<Pet> listarPorEstado(@PathVariable("estado") String estado) {
        return service.buscarPorEstado(estado);
    }
}
