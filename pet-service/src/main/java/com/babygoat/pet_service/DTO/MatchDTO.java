package com.babygoat.pet_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchDTO {
    private Long petId;
    private Long userId;
    private String breed;
    private String color;
    private String location;
    private String status;
}
