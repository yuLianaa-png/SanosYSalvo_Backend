package com.babygoat.notification_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetDTO {
    private Long userId;
    private Long id;
    private String breed;
    private String color;
    private String status; // "LOST" or "FOUND"
    private String location;
}
