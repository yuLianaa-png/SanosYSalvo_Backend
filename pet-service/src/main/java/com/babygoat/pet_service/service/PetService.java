package com.babygoat.pet_service.service;

import com.babygoat.pet_service.DTO.MatchDTO;
import com.babygoat.pet_service.DTO.PetDTO;
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
    public Pet registrarMascota(Pet mascota, String identity) {
        UsuarioDTO usuario;
        try {
            usuario = authCliente.obtenerUsuarioPorNombre(identity);
            if (usuario == null) {
                throw new RuntimeException("El usuario autenticado no existe en la base de datos.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error crítico: No se pudo validar la identidad del token. " + e.getMessage());
        }

        Long realUserId = usuario.getId();
        mascota.setContactoTutor(usuario.getTelefono());
        mascota.setUsuarioId(realUserId);

        Pet mascotaGuardada = petRepository.save(mascota);

        if (mascotaGuardada.getEstado() != null &&
                (mascotaGuardada.getEstado().equalsIgnoreCase("PERDIDA") ||
                        mascotaGuardada.getEstado().equalsIgnoreCase("ENCONTRADA"))) {

            try {
                MatchDTO aviso = new MatchDTO();
                aviso.setPetId(mascotaGuardada.getId());
                aviso.setUserId(mascotaGuardada.getUsuarioId());
                aviso.setRaza(mascotaGuardada.getRaza());
                aviso.setColor(mascotaGuardada.getColor());
                aviso.setUbicacion(mascotaGuardada.getUbicacion());
                aviso.setEstado(mascotaGuardada.getEstado());

                matchCliente.avisarNuevoMatch(aviso);

                System.out.println("DEBUG: Aviso de match enviado exitosamente para mascota: " + mascotaGuardada.getId());

            } catch (Exception e) {
                System.err.println("Error detallado del Match-Service: " + e.getMessage());
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
    public List<PetDTO> buscarPorRazaYColor(String raza, String color, String ubicacion, String estado) {
        return petRepository.buscarCoincidenciasManual(raza, color, estado, ubicacion);
    }

    //mostrar mascotas según su estado
    public List<Pet> buscarPorEstado(String estado) {
        List<Pet> todas = petRepository.findByEstadoIgnoreCase(estado);
        return todas.stream()
                .filter(p -> p.getEstado().equalsIgnoreCase(estado))
                .collect(Collectors.toList());
    }
}
