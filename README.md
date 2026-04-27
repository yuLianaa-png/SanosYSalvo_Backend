# Sistema de Gestión de Mascotas "Sanos y Salvos"

Este proyecto es una solución basada en Microservicios diseñada para la gestión y recuperación de mascotas perdidas. La
arquitectura permite el registro de hallazgos y un motor de coincidencias inteligente para conectar mascotas perdidas
con sus dueños.

Arquitectura del Sistema
La solución se compone de tres contenedores principales orquestados con Docker Compose:

- Pet-Service (Puerto 8081): Microservicio encargado de la persistencia de datos (CRUD) de mascotas.

- Match-Service (Puerto 8082): Microservicio de lógica de negocio que utiliza Netflix Feign para consumir datos del
  Pet-Service y aplicar filtros de búsqueda.

- Database (Puerto 5433): Instancia de PostgreSQL 15 para el almacenamiento persistente.

- Requisitos Previos
  Java 17 (Eclipse Temurin recomendado)

- Maven 3.8+

- Docker & Docker Desktop

- Postman (para pruebas de API)

## Instalación y Despliegue

Siga estos pasos para levantar el entorno completo:

### Compilar los microservicios:

Desde la carpeta raíz, ejecute:

- Bash
  cd pet-service && ./mvnw clean package -DskipTests && cd ..
  cd match-service && ./mvnw clean package -DskipTests && cd ..

Levantar la infraestructura:

- Bash
  docker-compose up --build -d

## Documentación de la API

1. Pet Service (Persistencia)
    - POST /api/pets: Registrar una nueva mascota.
    - GET /api/pets: Obtener el listado completo.

2. Match Service (Lógica de Negocio)
    - GET /api/matches/buscar?raza=X&color=Y: Busca coincidencias por raza y color mediante comunicación inter-servicio.
    - GET /api/matches/por-estado?estado=PERDIDA: Filtra mascotas según su estado actual.

## Persistencia de Datos

El sistema utiliza Volúmenes de Docker mapeados a la carpeta ./postgres_data. Esto garantiza que los datos no se pierdan
al ejecutar docker-compose down.

## Tecnologías Utilizadas

- Spring Boot 4.0.6

- Spring Data JPA / Hibernate

- Spring Cloud OpenFeign (Comunicación REST)

- PostgreSQL 15

- Docker & Docker Compose