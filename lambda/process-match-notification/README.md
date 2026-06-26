# process-match-notification Lambda

Esta función AWS Lambda procesa mensajes recibidos desde Amazon SQS y crea notificaciones en `notification-service`.

## Flujo

1. `match-service` detecta posibles coincidencias entre mascotas perdidas y encontradas.
2. `match-service` publica un mensaje en Amazon SQS.
3. SQS activa esta función Lambda.
4. La Lambda lee las notificaciones del mensaje.
5. La Lambda llama al endpoint interno de `notification-service`.
6. `notification-service` guarda las notificaciones en PostgreSQL.

## Variables de entorno

La función requiere las siguientes variables:

| Variable                  | Descripción                                               |
|---------------------------|-----------------------------------------------------------|
| `API_BASE_URL`            | URL base del API Gateway                                  |
| `INTERNAL_SERVICE_SECRET` | Clave interna usada para validar llamadas entre servicios |

Ejemplo:

```txt
API_BASE_URL=https://c2r4lbaie4.execute-api.us-east-1.amazonaws.com
INTERNAL_SERVICE_SECRET={contraseña-secreta}
```

## Endpoint llamado por la Lambda

La Lambda llama al siguiente endpoint interno:

---> **POST /api/notifications/internal/match**

**Headers utilizados:**

* Content-Type: application/json
* X-Internal-Secret: <INTERNAL_SERVICE_SECRET>

## Formato esperado del mensaje SQS

Ejemplo de mensaje recibido desde SQS:

```
{
"notifications": [
        {
        "userId": 1,
        "message": "We found possible matches for your pet.",
        "suggestions": [
                {
                "petId": 2,
                "breed": "Poodle",
                "color": "Blanco",
                "location": "Viña del Mar, Chile",
                "status": "FOUND",
                "userId": 2
                }
               ]
        }
    ]
}
```

## Prueba

La función puede probarse desde la consola de AWS Lambda usando un evento de prueba con formato de evento SQS.

También puede probarse indirectamente desde el flujo completo del sistema:

```
POST /api/v1/mascotas
    → match-service
    → SQS
    → Lambda
    → notification-service
    → GET /api/notifications/my
```

## Seguridad

El endpoint interno de ```notification-service``` valida el header ```X-Internal-Secret```, por lo que la Lambda debe
enviar la
clave interna configurada en sus variables de entorno.

El usuario final no llama directamente a esta Lambda.
