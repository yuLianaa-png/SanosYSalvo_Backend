# process-match-notification

Función AWS Lambda encargada de procesar mensajes provenientes de la cola SQS `match-notifications-queue`.

## Objetivo

Esta función actúa como consumidor serverless del flujo de notificaciones de matches.

Flujo esperado:

1. `match-service` detecta coincidencias entre mascotas perdidas y encontradas.
2. `match-service` publica un mensaje en AWS SQS.
3. AWS Lambda consume el mensaje desde la cola.
4. Lambda envía la notificación procesada hacia `notification-service`.

## Estado

Pendiente de implementación final hasta definir:

- URL final de Notification Service
- Cola SQS definitiva
- Estrategia de autenticación entre Lambda y backend