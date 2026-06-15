package com.babygoat.match_service.DTO;

public class MatchDTO {
    private Long petId;
    private Long userId;
    private String breed;
    private String color;
    private String location;
    private String status;

    public MatchDTO() {
    }

    public MatchDTO(Long petId, Long userId, String breed, String color, String location, String status) {
        this.petId = petId;
        this.userId = userId;
        this.breed = breed;
        this.color = color;
        this.location = location;
        this.status = status;
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
