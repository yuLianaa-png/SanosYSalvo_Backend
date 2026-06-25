# Sanos y Salvos - Sistema de Recuperación de Mascotas

Sistema basado en una arquitectura de microservicios diseñado para facilitar el encuentro de mascotas perdidas y
encontradas mediante un motor de coincidencias y notificaciones automatizadas.

El proyecto fue desplegado en AWS utilizando contenedores Docker, Docker Swarm, API Gateway, Amazon SQS, AWS Lambda y
GitHub Actions como pipeline CI/CD.

---

## Arquitectura del Sistema

El sistema se compone de microservicios independientes, cada uno con su propia base de datos PostgreSQL. Los servicios
se comunican mediante Feign Clients y utilizan JWT para proteger los endpoints de usuario.

Además, el sistema incorpora una arquitectura cloud con los siguientes componentes:

* **API Gateway**: expone el backend hacia el exterior.
* **VPC Link + ALB interno**: conecta API Gateway con servicios privados dentro de la VPC.
* **Nginx**: funciona como reverse proxy hacia los microservicios.
* **Docker Swarm**: orquesta los microservicios en instancias EC2 privadas.
* **EC2 Bastion**: permite acceso SSH seguro hacia las instancias privadas.
* **EC2 Manager**: administra el clúster Docker Swarm.
* **EC2 Worker**: ejecuta la mayoría de los servicios de aplicación.
* **Amazon ECR**: almacena las imágenes Docker del sistema.
* **Amazon SQS**: desacopla la generación de coincidencias del procesamiento de notificaciones.
* **AWS Lambda**: consume mensajes desde SQS y crea notificaciones en `notification-service`.
* **PostgreSQL**: base de datos relacional utilizada por cada microservicio.

---

## Microservicios

### Auth-Service

Servicio encargado de la gestión de usuarios, registro, login y emisión de tokens JWT.

Responsabilidades principales:

* Registro de usuarios.
* Inicio de sesión.
* Generación de JWT.
* Búsqueda de usuarios para uso interno de otros servicios.

---

### Pet-Service

Servicio encargado de la administración del ciclo de vida de las mascotas registradas.

Responsabilidades principales:

* Registro de mascotas perdidas o encontradas.
* Asociación de mascotas al usuario autenticado.
* Consulta de mascotas.
* Envío de información a `match-service` cuando se registra una mascota perdida o encontrada.

---

### Match-Service

Servicio encargado del motor de búsqueda de coincidencias.

Responsabilidades principales:

* Recibir información de una mascota registrada.
* Buscar coincidencias por raza, color, ubicación y estado.
* Crear registros de coincidencias.
* Publicar mensajes en Amazon SQS cuando existen posibles coincidencias.

---

### Notification-Service

Servicio encargado de la creación y consulta de notificaciones.

Responsabilidades principales:

* Recibir notificaciones internas desde AWS Lambda.
* Persistir notificaciones en PostgreSQL.
* Permitir que cada usuario consulte únicamente sus propias notificaciones mediante JWT.

El endpoint seguro para usuario es:

```txt
GET /api/notifications/my
```

Este endpoint obtiene el usuario desde el token JWT, evitando que un usuario consulte notificaciones de otro usuario
manipulando un `userId` en la URL.

---

## Flujo Principal del Sistema

1. El usuario se registra o inicia sesión.
2. El usuario obtiene un token JWT.
3. El usuario registra una mascota perdida o encontrada.
4. `pet-service` guarda la mascota.
5. `pet-service` llama internamente a `match-service`.
6. `match-service` busca posibles coincidencias.
7. Si existen coincidencias, `match-service` publica un mensaje en Amazon SQS.
8. Amazon SQS activa la función AWS Lambda.
9. Lambda llama al endpoint interno de `notification-service`.
10. `notification-service` guarda la notificación.
11. El usuario consulta sus notificaciones mediante `/api/notifications/my`.

---

## Comunicación Asíncrona con SQS y Lambda

Para desacoplar el proceso de coincidencias del proceso de notificaciones, se implementó una cola en Amazon SQS.

Flujo asíncrono:

```txt
match-service
→ Amazon SQS
→ AWS Lambda
→ notification-service
→ notification-db
```

Esto permite que `match-service` no dependa directamente de la disponibilidad inmediata de `notification-service`,
mejorando la resiliencia y separación de responsabilidades del sistema.

---

## Seguridad

El sistema utiliza dos mecanismos principales de seguridad:

### 1. JWT para usuarios

Los endpoints de usuario requieren el header:

```txt
Authorization: Bearer <token_jwt>
```

Esto permite identificar al usuario autenticado y restringir el acceso a sus propios recursos.

### 2. X-Internal-Secret para comunicación interna

Algunos endpoints internos no son llamados directamente por usuarios, sino por otros servicios o por AWS Lambda.

Estos endpoints validan el header:

```txt
X-Internal-Secret: <clave_interna>
```

Ejemplos de endpoints internos:

```txt
POST /api/matches/create
POST /api/notifications/internal/match
```

---

## Dependencias Core del Proyecto

Cada microservicio utiliza librerías de Spring Boot y Spring Cloud para garantizar su funcionamiento.

### Comunicación y Nube

* **OpenFeign**: comunicación declarativa entre microservicios.
* **Spring Cloud Dependencies**: gestión centralizada de versiones para microservicios.
* **AWS SDK SQS**: publicación de mensajes desde `match-service` hacia Amazon SQS.

### Seguridad y Autenticación

* **Spring Boot Starter Security**: implementación de filtros de seguridad y políticas de acceso.
* **JJWT**: generación, firma y validación de tokens JWT.

### Persistencia de Datos

* **Spring Data JPA**: manejo de repositorios y persistencia.
* **PostgreSQL Driver**: conexión con PostgreSQL.
* **Hibernate**: mapeo ORM de entidades Java hacia tablas relacionales.

### Utilidades y Serialización

* **Lombok**: reducción de código repetitivo.
* **Jackson**: serialización y deserialización JSON.
* **Spring Boot Starter Web**: creación de APIs RESTful con servidor embebido.

---

## Stack Tecnológico

* **Lenguaje:** Java 17
* **Framework:** Spring Boot / Spring Cloud
* **Seguridad:** Spring Security + JWT
* **Base de Datos:** PostgreSQL
* **Contenedores:** Docker
* **Orquestación:** Docker Swarm
* **Reverse Proxy:** Nginx
* **Cloud Provider:** AWS
* **API Management:** API Gateway
* **Mensajería:** Amazon SQS
* **Serverless:** AWS Lambda
* **Registro de imágenes:** Amazon ECR
* **CI/CD:** GitHub Actions
* **Infraestructura:** EC2 Bastion, EC2 Manager y EC2 Worker

---

## Estructura General del Proyecto

```txt
SanosYSalvo_Backend/
├── auth-service/
├── pet-service/
├── match-service/
├── notification-service/
├── nginx/
├── lambda/
│   └── process-match-notification/
│       ├── lambda_function.py
│       └── README.md
├── .github/
│   └── workflows/
│       └── ci-cd.yml
├── docker-compose.yml
├── docker-stack.prod.yml
├── .gitignore
└── README.md
```

---

## API Endpoints Principales

### Auth-Service

| Método | Endpoint                      | Descripción                                       |
|--------|-------------------------------|---------------------------------------------------|
| `POST` | `/api/auth/register`          | Registro de nuevos usuarios                       |
| `POST` | `/api/auth/login`             | Inicio de sesión y generación de JWT              |
| `GET`  | `/api/auth/user/{id}`         | Obtención de usuario por ID                       |
| `GET`  | `/api/auth/search/{username}` | Búsqueda de usuario por username para uso interno |

---

### Pet-Service

| Método | Endpoint                          | Descripción                                     |
|--------|-----------------------------------|-------------------------------------------------|
| `POST` | `/api/v1/mascotas`                | Registro de mascota perdida o encontrada        |
| `GET`  | `/api/v1/mascotas/{id}`           | Consulta de mascota por ID                      |
| `GET`  | `/api/v1/mascotas/search/matches` | Búsqueda interna de mascotas para coincidencias |

---

### Match-Service

| Método | Endpoint                     | Descripción                                         |
|--------|------------------------------|-----------------------------------------------------|
| `POST` | `/api/matches/create`        | Endpoint interno para crear y evaluar coincidencias |
| `GET`  | `/api/matches`               | Listado de coincidencias                            |
| `GET`  | `/api/matches/user/{userId}` | Coincidencias asociadas a un usuario                |

---

### Notification-Service

| Método | Endpoint                            | Descripción                                                     |
|--------|-------------------------------------|-----------------------------------------------------------------|
| `POST` | `/api/notifications/internal/match` | Endpoint interno usado por AWS Lambda para crear notificaciones |
| `GET`  | `/api/notifications/my`             | Consulta segura de notificaciones del usuario autenticado       |

---

## Variables de Entorno Principales

El sistema utiliza variables de entorno para configurar bases de datos, comunicación interna y servicios cloud.

Ejemplo de variables necesarias:

```env
REGION=us-east-1
AWS_REGION=us-east-1
ACCOUNT_ID=<aws_account_id>
ECR_REGISTRY=<account_id>.dkr.ecr.us-east-1.amazonaws.com

AUTH_SERVICE_URL=http://auth-service:8080
PET_SERVICE_URL=http://pet-service:8080
MATCH_SERVICE_URL=http://match-service:8080
NOTIFICATION_SERVICE_URL=http://notification-service:8080

SQS_QUEUE_URL=<sqs_queue_url>
INTERNAL_SERVICE_SECRET=<internal_secret>

SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

El archivo `.env.prod` no debe subirse al repositorio.

---

## Despliegue Local con Docker Compose

Para levantar el sistema localmente:

```bash
docker-compose up --build
```

---

## Despliegue en Producción con Docker Swarm

Desde la instancia manager:

```bash
set -a
source .env.prod
set +a

docker stack deploy --with-registry-auth -c docker-stack.prod.yml sanos
```

Ver servicios desplegados:

```bash
docker service ls
```

Ver distribución de servicios en los nodos:

```bash
docker service ps sanos_auth-service
docker service ps sanos_pet-service
docker service ps sanos_match-service
docker service ps sanos_notification-service
docker service ps sanos_nginx
```

---

## Docker Swarm

El clúster Docker Swarm está compuesto por:

* **Manager:** administra el estado del clúster.
* **Worker:** ejecuta servicios de aplicación.
* **Bastion:** permite acceso SSH seguro hacia la red privada.

Comando para ver nodos:

```bash
docker node ls
```

Ejemplo de escalado:

```bash
docker service scale sanos_pet-service=2
```

Ver réplicas:

```bash
docker service ps sanos_pet-service
```

Volver a una réplica:

```bash
docker service scale sanos_pet-service=1
```

---

## CI/CD con GitHub Actions

El proyecto incluye un pipeline CI/CD utilizando GitHub Actions.

El pipeline se ejecuta al hacer push a la rama:

```txt
ci-cd
```

El flujo del pipeline es:

1. Clonar el repositorio.
2. Configurar Java 17.
3. Compilar los microservicios con Maven.
4. Construir imágenes Docker.
5. Publicar imágenes en Amazon ECR.
6. Conectarse por SSH al bastion público.
7. Saltar hacia la instancia manager privada.
8. Actualizar los servicios en Docker Swarm.

Flujo resumido:

```txt
GitHub Actions
→ Build Maven
→ Docker Build
→ Push a ECR
→ SSH Bastion
→ SSH Manager
→ Docker Swarm Update
```

---

## AWS Lambda

El código fuente de la función Lambda se encuentra en:

```txt
lambda/process-match-notification/lambda_function.py
```

La función procesa mensajes recibidos desde Amazon SQS y llama al endpoint interno:

```txt
POST /api/notifications/internal/match
```

La Lambda utiliza variables de entorno para obtener:

```txt
API_BASE_URL
INTERNAL_SERVICE_SECRET
```

---

## Limitación sobre Amazon RDS

Se evaluó migrar PostgreSQL hacia Amazon RDS, pero el entorno AWS Academy utilizado no cuenta con permisos IAM para
ejecutar:

```txt
rds:CreateDBInstance
```

Por esta razón, las bases de datos PostgreSQL fueron desplegadas como contenedores dentro del clúster Docker Swarm en
subred privada.

Como mejora futura, se propone migrar las bases de datos hacia Amazon RDS PostgreSQL para delegar respaldos,
mantenimiento, disponibilidad y administración del motor de base de datos en un servicio administrado.

---

## Comandos Útiles

Ver servicios:

```bash
docker service ls
```

Ver logs de un servicio:

```bash
docker service logs sanos_pet-service --since 5m --no-trunc
```

Actualizar un servicio manualmente:

```bash
docker service update --force sanos_pet-service
```

Ver imagen usada por un servicio:

```bash
docker service inspect sanos_pet-service --format '{{.Spec.TaskTemplate.ContainerSpec.Image}}'
```

---

## Estado del Proyecto

El sistema cuenta con:

* Microservicios funcionales.
* Docker Swarm con manager y worker.
* API Gateway como entrada pública.
* Nginx como reverse proxy.
* Comunicación interna protegida.
* Amazon SQS para mensajería asíncrona.
* AWS Lambda para procesamiento serverless.
* GitHub Actions para CI/CD.
* Endpoint seguro para notificaciones de usuario autenticado.
