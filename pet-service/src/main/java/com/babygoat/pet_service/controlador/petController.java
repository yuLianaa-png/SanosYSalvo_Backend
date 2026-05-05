//http

package com.babygoat.pet_service.controlador;


import com.babygoat.pet_service.DTO.PetDTO;
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

    // Constructor para permitir inyección en tests y mantener compatibilidad con Spring
    public petController() {
    }

    public petController(PetService service, petRepository petRepository) {
        this.service = service;
        this.petRepository = petRepository;
    }

    //Registra mascota
    @PostMapping("/registrar")
    public ResponseEntity<Pet> registrar(@RequestBody Pet pet) {
        String identity = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();

        return ResponseEntity.ok(service.registrarMascota(pet, identity));
    }

    //Actualiza detalle de mascota (funciona pero hay que mejorar)
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

    //buscar mascotas por raza y color !!
    @GetMapping("/buscar/match")
    public ResponseEntity<List<PetDTO>> buscarCoincidencias(
            @RequestParam(required = false) String raza,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String ubicacion) {

        // Normalizar: trim y convertir cadena vacía a null
        raza = (raza != null && !raza.trim().isEmpty()) ? raza.trim() : null;
        color = (color != null && !color.trim().isEmpty()) ? color.trim() : null;
        estado = (estado != null && !estado.trim().isEmpty()) ? estado.trim() : null;
        ubicacion = (ubicacion != null && !ubicacion.trim().isEmpty()) ? ubicacion.trim() : null;

        List<PetDTO> resultados = service.buscarPorRazaYColor(raza, color, ubicacion, estado);

        if (resultados == null || resultados.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resultados);
    }

    //mostrar mascotas según su estado
    @GetMapping("/estado/{estado}")
    public List<Pet> listarPorEstado(@PathVariable("estado") String estado) {
        return service.buscarPorEstado(estado);
    }
}
