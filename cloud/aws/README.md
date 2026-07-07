# Configuración Cloud AWS

Esta carpeta contiene scripts y documentación para configurar recursos cloud utilizados por el sistema Sanos y Salvos.

## Recursos gestionados

El script `setup-sqs-lambda.sh` permite configurar los siguientes recursos:

- Amazon SQS
- AWS Lambda
- Trigger SQS hacia Lambda
- Variables de entorno de Lambda
- Actualización del código fuente de Lambda

## Recursos utilizados por el sistema

| Recurso      | Uso                                            |
|--------------|------------------------------------------------|
| API Gateway  | Entrada pública del backend                    |
| VPC Link     | Conexión privada entre API Gateway y la VPC    |
| ALB interno  | Balanceo hacia Nginx en la red privada         |
| Amazon ECR   | Registro de imágenes Docker                    |
| Docker Swarm | Orquestación de microservicios                 |
| Amazon SQS   | Cola de mensajes para notificaciones           |
| AWS Lambda   | Procesamiento serverless de mensajes desde SQS |
| PostgreSQL   | Base de datos por microservicio                |

## Script principal

```bash
cloud/aws/setup-sqs-lambda.sh
```

## Variables necesarias

Antes de ejecutar el script se deben definir las siguientes variables:

```
export AWS_REGION=us-east-1
export SQS_QUEUE_NAME=match-notifications-queue
export LAMBDA_FUNCTION_NAME=process-match-notification
export API_BASE_URL=https://c2r4lbaie4.execute-api.us-east-1.amazonaws.com
export INTERNAL_SERVICE_SECRET=<valor_secreto>
```

## Ejecución manual

Desde la raíz del proyecto:

```
chmod +x cloud/aws/setup-sqs-lambda.sh
cloud/aws/setup-sqs-lambda.sh
Integración con CI/CD
```

Este script puede ejecutarse desde GitHub Actions para que el pipeline no solo compile y despliegue microservicios, sino
que también configure recursos cloud necesarios para el flujo asíncrono de notificaciones.

## Restricciones de AWS Academy

El entorno AWS Academy puede limitar permisos para crear ciertos recursos, como roles IAM o Amazon RDS.

Por esta razón:

* La función Lambda puede requerir creación inicial desde consola.
* El script actualiza la Lambda existente.
* Amazon RDS no fue implementado debido a restricciones de permisos.
* PostgreSQL se ejecuta como contenedor dentro del clúster Docker Swarm en subred privada.