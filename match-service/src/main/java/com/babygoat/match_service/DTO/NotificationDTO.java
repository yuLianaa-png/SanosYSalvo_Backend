package com.babygoat.match_service.DTO;

import java.util.List;

public class NotificationDTO {
    private Long userId;
    private String message;
    private List<PetDTO> suggestions;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<PetDTO> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<PetDTO> suggestions) {
        this.suggestions = suggestions;
    }
}
