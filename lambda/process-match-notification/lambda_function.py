import json
import os
import urllib.request
import urllib.error


API_BASE_URL = os.environ.get("API_BASE_URL")
INTERNAL_SERVICE_SECRET = os.environ.get("INTERNAL_SERVICE_SECRET")


def post_to_notification_service(notification):
    url = f"{API_BASE_URL}/api/notifications/internal/match"

    data = json.dumps(notification).encode("utf-8")

    request = urllib.request.Request(
        url=url,
        data=data,
        method="POST",
        headers={
            "Content-Type": "application/json",
            "X-Internal-Secret": INTERNAL_SERVICE_SECRET
        }
    )

    with urllib.request.urlopen(request, timeout=10) as response:
        response_body = response.read().decode("utf-8")
        print(f"Notification sent. Status: {response.status}")
        print(f"Response body: {response_body}")
        return response.status


def lambda_handler(event, context):
    print("=== process-match-notification ejecutada ===")
    print(json.dumps(event, indent=2, ensure_ascii=False))

    for record in event.get("Records", []):
        body = record.get("body", "{}")

        try:
            message = json.loads(body)
        except json.JSONDecodeError:
            print("El mensaje recibido no es JSON válido:")
            print(body)
            continue

        notifications = message.get("notifications", [])

        if not notifications:
            print("El mensaje no contiene notificaciones.")
            continue

        for notification in notifications:
            print("Enviando notificación:")
            print(json.dumps(notification, indent=2, ensure_ascii=False))

            try:
                post_to_notification_service(notification)
            except urllib.error.HTTPError as e:
                error_body = e.read().decode("utf-8")
                print(f"Error HTTP al llamar notification-service: {e.code}")
                print(error_body)
                raise

            except Exception as e:
                print("Error general al llamar notification-service")
                print(str(e))
                raise

    return {
        "statusCode": 200,
        "body": "Mensajes procesados correctamente"
    }