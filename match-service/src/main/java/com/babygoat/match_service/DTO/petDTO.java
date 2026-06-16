package com.babygoat.match_service.DTO;

public class petDTO {
    private Long userId;
    private Long id;
    private String breed;
    private String color;
    private String status;
    private String location;

    public petDTO() {
    }

    public petDTO(Long userId, Long id, String breed, String color, String status, String location) {
        this.userId = userId;
        this.id = id;
        this.breed = breed;
        this.color = color;
        this.status = status;
        this.location = location;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}