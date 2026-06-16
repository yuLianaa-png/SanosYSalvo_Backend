package com.babygoat.pet_service.Service;

import com.babygoat.pet_service.DTO.MatchDTO;
import com.babygoat.pet_service.DTO.PetDTO;
import com.babygoat.pet_service.DTO.UserDTO;
import com.babygoat.pet_service.Model.Pet;
import com.babygoat.pet_service.Repository.AuthClient;
import com.babygoat.pet_service.Repository.MatchClient;
import com.babygoat.pet_service.Repository.petRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    private final petRepository petRepository;
    private final MatchClient matchClient;
    private final AuthClient authClient;

    public PetService(petRepository petRepository,
                      MatchClient matchClient,
                      AuthClient authClient) {
        this.petRepository = petRepository;
        this.matchClient = matchClient;
        this.authClient = authClient;
    }

    public Pet registerPet(Pet pet, String identity) {

        UserDTO user = authClient.getUserByUsername(identity);

        if (user == null) {
            throw new RuntimeException("User not found in auth service");
        }

        pet.setUserId(user.getId());
        pet.setTutorContact(user.getPhone());

        Pet savedPet = petRepository.save(pet);

        notifyMatchIfNeeded(savedPet);

        return savedPet;
    }

    private void notifyMatchIfNeeded(Pet pet) {

        if (pet.getStatus() == null) return;

        String status = pet.getStatus().toUpperCase();

        if (!status.equals("LOST") && !status.equals("FOUND")) return;

        try {
            MatchDTO dto = new MatchDTO();
            dto.setPetId(pet.getId());
            dto.setUserId(pet.getUserId());
            dto.setBreed(pet.getBreed());
            dto.setColor(pet.getColor());
            dto.setLocation(pet.getLocation());
            dto.setStatus(pet.getStatus());

            matchClient.notifyNewMatch(dto);

        } catch (Exception e) {
            System.err.println("Match service error: " + e.getMessage());
        }
    }

    public Pet updatePet(Long id, Pet details) {

        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        pet.setName(details.getName());
        pet.setBreed(details.getBreed());
        pet.setColor(details.getColor());
        pet.setSize(details.getSize());
        pet.setStatus(details.getStatus());
        pet.setLocation(details.getLocation());

        return petRepository.save(pet);
    }

    public void deletePet(Long id) {

        if (!petRepository.existsById(id)) {
            throw new RuntimeException("Pet not found");
        }

        petRepository.deleteById(id);
    }

    public List<Pet> listAll() {
        return petRepository.findAll();
    }

    public Optional<Pet> getById(Long id) {
        return petRepository.findById(id);
    }

    public List<PetDTO> searchByBreedAndColor(String breed, String color, String location, String status) {
        return petRepository.searchMatchesManual(breed, color, location, status);
    }

    public List<Pet> searchByStatus(String status) {
        return petRepository.findByStatusIgnoreCase(status);
    }
}