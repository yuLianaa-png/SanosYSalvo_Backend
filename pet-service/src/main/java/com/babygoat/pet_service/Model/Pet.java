package com.babygoat.pet_service.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "breed", nullable = false, length = 50)
    private String breed;

    @Column(name = "color", nullable = true, length = 30)
    private String color;

    @Column(name = "size", nullable = true, length = 20)
    private String size;

    @Column(name = "status", nullable = false, length = 10)
    private String status; // "LOST" or "FOUND"

    @Column(name = "location", nullable = true, length = 100)
    private String location;

    @Column(name = "tutor_contact", nullable = false, length = 50)
    private String tutorContact;

    @Column(name = "report_date", nullable = false)
    private LocalDateTime reportDate = LocalDateTime.now();

    // GETTERS Y SETTERS

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getTutorContact() { return tutorContact; }
    public void setTutorContact(String tutorContact) { this.tutorContact = tutorContact; }

    public LocalDateTime getReportDate() { return reportDate; }
    public void setReportDate(LocalDateTime reportDate) { this.reportDate = reportDate; }
}