package com.babygoat.pet_service.service;

import com.babygoat.pet_service.DTO.MatchDTO;
import com.babygoat.pet_service.DTO.UsuarioDTO;
import com.babygoat.pet_service.model.Pet;
import com.babygoat.pet_service.repository.AuthCliente;
import com.babygoat.pet_service.repository.MatchCliente;
import com.babygoat.pet_service.repository.petRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetService {
    @Autowired
    private petRepository petRepository;

    @Autowired
    private MatchCliente matchCliente;

    @Autowired
    private AuthCliente authCliente;

    //Registra mascota
    public Pet registrarMascota(Pet mascota, Long userId) {
        try {
            UsuarioDTO usuario = authCliente.obtenerUsuarioPorId(userId);
            // se vincula el teléfono automáticamente
            mascota.setContactoTutor(usuario.getTelefono());
            mascota.setUsuarioId(userId);
        } catch (Exception e) {
            System.err.println("No se pudo obtener el teléfono del usuario, se guardará sin él.");
        }

        Pet mascotaGuardada = petRepository.save(mascota);

        //Si el estado es "Perdida" o "Encontrada", disparamos el match automático
        if (mascota.getEstado() != null &&
                (mascota.getEstado().equalsIgnoreCase("PERDIDA") ||
                        mascota.getEstado().equalsIgnoreCase("ENCONTRADA"))) {

            MatchDTO matchReq = new MatchDTO();
            matchReq.setPetId(mascotaGuardada.getId());
            matchReq.setUserId(userId);

            try {
                matchCliente.avisarNuevoMatch(matchReq);
            } catch (Exception e) {
                // Logeamos el error pero no detenemos el registro de la mascota
                System.out.println("No se pudo crear el match automático, pero la mascota se guardó.");
            }
        }

        return mascotaGuardada;
    }

    //Actualiza detalle de mascota
    public Pet actualizarMascota(Long id, Pet detalles) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
        pet.setNombre(detalles.getNombre());
        pet.setRaza(detalles.getRaza());
        pet.setColor(detalles.getColor());
        pet.setTamano(detalles.getTamano());
        pet.setEstado(detalles.getEstado());
        pet.setUbicacion(detalles.getUbicacion()); // Importante para cambiar de PERDIDA a ENCONTRADA
        return petRepository.save(pet);
    }

    //Elimina mascota
    public void eliminarMascota(Long id) {
        if (!petRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar: Mascota no encontrada");
        }
        petRepository.deleteById(id);
    }

    //Lista todas las mascotas que hay en el sistema
    public List<Pet> listarTodas() {
        return petRepository.findAll();
    }

    //buscar mascotas por raza y color
    public List<Pet> buscarPorRazaYColor(String raza, String color) {
        return petRepository.findByRazaIgnoreCaseAndColorIgnoreCase(raza, color);
    }

    //mostrar mascotas según su estado
    public List<Pet> buscarPorEstado(String estado) {
        List<Pet> todas = petRepository.findByEstadoIgnoreCase(estado);
        return todas.stream()
                .filter(p -> p.getEstado().equalsIgnoreCase(estado))
                .collect(Collectors.toList());
    }
}
