package com.babygoat.match_service.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SqsNotificationPublisher {

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;
    private final String queueUrl;

    public SqsNotificationPublisher(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;

        String awsRegion = System.getenv("AWS_REGION");
        this.queueUrl = System.getenv("SQS_QUEUE_URL");

        if (awsRegion == null || awsRegion.isBlank()) {
            throw new IllegalStateException("AWS_REGION no está configurada");
        }

        if (queueUrl == null || queueUrl.isBlank()) {
            throw new IllegalStateException("SQS_QUEUE_URL no está configurada");
        }

        this.sqsClient = SqsClient.builder()
                .region(Region.of(awsRegion))
                .build();
    }

    public void publishNotifications(List<?> notifications) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("notifications", notifications);

            String body = objectMapper.writeValueAsString(message);

            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(body)
                    .build();

            sqsClient.sendMessage(request);

            System.out.println("Mensaje enviado correctamente a SQS:");
            System.out.println(body);

        } catch (Exception e) {
            System.out.println("Error enviando mensaje a SQS");
            System.out.println(e.getMessage());
            throw new RuntimeException("No se pudo enviar mensaje a SQS", e);
        }
    }
}
