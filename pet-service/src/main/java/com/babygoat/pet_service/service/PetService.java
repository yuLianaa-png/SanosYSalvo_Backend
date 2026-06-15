package com.babygoat.pet_service.Service;

import com.babygoat.pet_service.DTO.MatchDTO;
import com.babygoat.pet_service.DTO.PetDTO;
import com.babygoat.pet_service.DTO.UserDTO;
import com.babygoat.pet_service.Model.Pet;
import com.babygoat.pet_service.Repository.AuthClient;
import com.babygoat.pet_service.Repository.MatchClient;
import com.babygoat.pet_service.Repository.petRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetService {
    @Autowired
    private petRepository petRepository;

    @Autowired
    private MatchClient matchClient;

    @Autowired
    private AuthClient authClient;

    public Pet registerPet(Pet pet, String identity) {
        UserDTO user;
        try {
            user = authClient.getUserByUsername(identity);
            if (user == null) {
                throw new RuntimeException("The authenticated user does not exist in the database.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Critical error: Could not validate token identity. " + e.getMessage());
        }

        Long realUserId = user.getId();
        pet.setTutorContact(user.getPhone());
        pet.setUserId(realUserId);

        Pet savedPet = petRepository.save(pet);

        if (savedPet.getStatus() != null &&
                (savedPet.getStatus().equalsIgnoreCase("LOST") ||
                        savedPet.getStatus().equalsIgnoreCase("FOUND"))) {

            try {
                MatchDTO notification = new MatchDTO();
                notification.setPetId(savedPet.getId());
                notification.setUserId(savedPet.getUserId());
                notification.setBreed(savedPet.getBreed());
                notification.setColor(savedPet.getColor());
                notification.setLocation(savedPet.getLocation());
                notification.setStatus(savedPet.getStatus());

                matchClient.notifyNewMatch(notification);

                System.out.println("DEBUG: Match notification sent successfully for pet: " + savedPet.getId());

            } catch (Exception e) {
                System.err.println("Error from Match-Service: " + e.getMessage());
            }
        }

        return savedPet;
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
            throw new RuntimeException("Cannot delete: Pet not found");
        }
        petRepository.deleteById(id);
    }

    public List<Pet> listAll() {
        return petRepository.findAll();
    }

    public List<PetDTO> searchByBreedAndColor(String breed, String color, String location, String status) {
        return petRepository.searchMatchesManual(breed, color, location, status);
    }

    // Show pets by their status
    public List<Pet> searchByStatus(String status) {
        List<Pet> all = petRepository.findByStatusIgnoreCase(status);
        return all.stream()
                .filter(p -> p.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }
}
