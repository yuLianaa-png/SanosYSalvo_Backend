package com.babygoat.pet_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetDTO {
    private Long id;
    private Long userId;
    private String name;
    private String breed;
    private String color;
    private String status;
    private String location;
}
