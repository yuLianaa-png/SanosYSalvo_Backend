# Deployment Guide - Sanos y Salvos Backend

## 1. Objetivo

Este documento describe el proceso de despliegue cloud del backend Sanos y Salvos.

La solución utiliza:

- Microservicios Spring Boot
- Docker
- AWS ECR
- Amazon VPC
- EC2 privadas
- Docker Swarm
- Nginx como reverse proxy interno
- AWS API Gateway
- AWS SQS
- AWS Lambda
- GitHub Actions

## 2. Arquitectura

El backend se despliega en instancias EC2 privadas dentro de una VPC.

El acceso externo se realiza mediante AWS API Gateway, que se conecta mediante VPC Link a un Load Balancer interno. Este
Load Balancer enruta tráfico al clúster Docker Swarm, donde Nginx distribuye las solicitudes hacia los microservicios.

Flujo general:

Cliente → AWS API Gateway → VPC Link → Load Balancer interno → EC2 privadas con Docker Swarm → Nginx → Microservicios

## 3. Servicios del sistema

- auth-service
- pet-service
- match-service
- notification-service
- nginx

## 4. Repositorios ECR

Repositorios usados:

- sanos-salvos-auth
- sanos-salvos-petserv
- sanos-salvos-match
- sanos-salvos-notif
- sanos-salvos-nginx

## 5. Docker Swarm

El clúster está compuesto por:

- sanos-manager
- sanos-worker

Inicialización del manager:

```bash
docker swarm init --advertise-addr IP_PRIVADA_MANAGER 
```

Unión del worker:

```bash
docker swarm join --token TOKEN IP_PRIVADA_MANAGER:2377
```

Despliegue del stack:

```bash
docker stack deploy --with-registry-auth -c docker-stack.prod.yml sanos
```

## 6. Escalamiento

Ejemplo de escalamiento:

```bash
docker service scale sanos_pet-service=3
docker service scale sanos_match-service=3
```

## 7. API Gateway

AWS API Gateway será configurado como punto de entrada externo.

Rutas esperadas:

ANY /api/auth/{proxy+}
ANY /api/v1/mascotas/{proxy+}
ANY /api/matches/{proxy+}
ANY /api/notifications/{proxy+}

## 8. SQS y Lambda

La cola match-notifications-queue permitirá desacoplar el proceso de generación de notificaciones.

Flujo:

match-service → SQS → Lambda → notification-service

## 9. CI/CD

El pipeline de GitHub Actions realizará:

Build del código.
Build de imágenes Docker.
Push a AWS ECR.
Despliegue en EC2 manager mediante Docker Swarm.