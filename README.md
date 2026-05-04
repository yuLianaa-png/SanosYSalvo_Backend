# Sanos y Salvos - Sistema de Recuperación de Mascotas

Sistema basado en una arquitectura de microservicios diseñado para facilitar el encuentro de mascotas perdidas mediante
un motor de coincidencias en tiempo real y notificaciones automatizadas.

## Arquitectura del Sistema

El proyecto se compone de microservicios independientes, cada uno con su propia base de datos PostgreSQL, comunicados
mediante Feign Clients y protegidos con JWT.

### Microservicios:

- _**Auth-Service:**_ Gestión de usuarios y emisión de tokens JWT para seguridad entre servicios.

- _**Pet-Service:**_ Administración del ciclo de vida de las mascotas registradas (Perdidas/Encontradas).

- _**Match-Service:**_ Motor de búsqueda de coincidencias basado en filtros geográficos, raza y color.

- _**Notification-Service:**_ Registro y persistencia de alertas de coincidencia para los usuarios.

## Dependencias Core del Proyecto

Cada microservicio utiliza las siguientes librerías de Spring Boot 3.x y Spring Cloud para garantizar su funcionamiento:

**1. Comunicación y Nube (Spring Cloud)**

- **OpenFeign:** Utilizado para la comunicación declarativa entre servicios (ej. Match-Service notificando al
  Notification-Service).
- **Spring Cloud Dependencies:** Gestión centralizada de versiones para microservicios.

**2. Seguridad y Autenticación**

- **Spring Boot Starter Security:** Implementación de filtros de seguridad y políticas de acceso.
- **JJWT (io.jsonwebtoken):** Generación, firma y validación de tokens JWT para asegurar la identidad entre nodos.

**3. Persistencia de Datos**

- **Spring Data JPA:** Abstracción para el manejo de repositorios y persistencia en base de datos.
- **PostgreSQL Driver:** Conector oficial para la base de datos relacional PostgreSQL.
- **Hibernate:** Motor ORM para el mapeo de entidades con soporte para estándares snake_case.

**4. Utilidades y Serialización**

- **Lombok:** Reducción de código repetitivo (Boilerplate) mediante anotaciones como _**@Data**_ y
  _**@AllArgsConstructor**_.
- **Jackson JSR310:** Módulo para la correcta serialización de fechas modernas como LocalDateTime en los DTOs.
- **Spring Boot Starter Web:** Servidor embebido Tomcat y soporte para APIs RESTful.

## Requisitos e Instalación

1. Clonar el repositorio:

   `git clone https://github.com/tu-usuario/sanos-salvos.git`

2. Compilar los servicios:

   `./mvn clean package -DskipTests`

3. Desplegar con Docker:

   `docker-compose up --build`

## Stack Tecnológico

* **Lenguaje:** Java 17

* **Framework:** Spring Boot 3.3.x / Spring Cloud

* **Seguridad:** Spring Security & JWT

* **Base de Datos:** PostgreSQL

* **Contenedores:** Docker & Docker Compose

* **Herramientas:** Lombok, Jackson (JSR310), Hibernate JPA

## API Endpoints

1. **Auth-Service (Puerto 8083)**
    * **POST** /api/auth/registrar: Registro de nuevos usuarios.
    * **POST** /api/auth/login: Autenticación y generación de token JWT.
    * **GET** /api/auth/buscar/{username}: Obtención de detalles de usuario (uso interno).


2. **Pet-Service (Puerto 8081)**
    * **POST** /api/mascotas/registrar: Registro de mascota (perdida o encontrada).

    * **GET** /api/mascotas: Listado completo de mascotas registradas.

    * **GET** /api/mascotas/buscar/match: (Uso Interno) Búsqueda filtrada por raza, color, ubicación y estado.

    * **PUT** /api/mascotas/{id}: Modificación de datos o estado de una mascota.

    * **DELETE** /api/mascotas/{id}: Eliminación de un registro de mascota.


3. **Match-Service (Puerto 8082)**
    * **POST** /api/matches/crear: Recibe una nueva mascota, busca coincidencias en Pet-Service y dispara notificaciones
      en Notification-Service. (Uso Interno)

    * **GET** /api/matches: Listado de todos los matches exitosos generados por el sistema.

    * **GET** /api/matches/Usuario/{UserId}: Devuelve las publicaciones hechas (match en la bd del usuario y el animal)


4. **Notification-Service (Puerto 8084)**
    * **POST** /api/notificaciones/enviar: (Uso Interno) Recibe y persiste alertas de match enviadas por el
      Match-Service.

    * **GET** /api/notificaciones/usuario/{userId}: Recupera todas las alertas pendientes para un usuario.

    * **DELETE** /api/notificaciones/limpiar/{userId}: Elimina el historial de notificaciones de un usuario. (AUN NO
      APLICADA)

## Notas de Implementación:

### Seguridad:

Todos los métodos (excepto Login y Registro) requieren el encabezado Authorization: Bearer <token_jwt>.

### Estandarización:

Las respuestas siguen el formato JSON y utilizan códigos de estado HTTP estándar (200 OK, 201 Created,
401 Unauthorized, 500 Error).