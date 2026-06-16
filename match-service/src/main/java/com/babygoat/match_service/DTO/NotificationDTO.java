package com.babygoat.match_service.DTO;

import java.util.List;

public class NotificationDTO {
    private Long userId;
    private String message;
    private List<petDTO> suggestions;

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

    public List<petDTO> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<petDTO> suggestions) {
        this.suggestions = suggestions;
    }
}
