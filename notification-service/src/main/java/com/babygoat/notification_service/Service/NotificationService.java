package com.babygoat.notification_service.Service;

import com.babygoat.notification_service.DTO.NotificationDTO;
import com.babygoat.notification_service.Model.Notification;
import com.babygoat.notification_service.Repository.NotificationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public void saveNotification(NotificationDTO dto) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Notification notif = new Notification();
            notif.setUserId(dto.getUserId());
            notif.setMessage(dto.getMessage());

            if (dto.getSuggestions() != null) {
                String jsonSuggestions = mapper.writeValueAsString(dto.getSuggestions());
                notif.setSuggestionsData(jsonSuggestions);
            }

            notificationRepository.save(notif);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Persistence error: " + e.getMessage());
        }
    }

    public List<Notification> getNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}
