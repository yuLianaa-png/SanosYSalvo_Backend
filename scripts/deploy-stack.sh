#!/bin/bash

set -e

if [ ! -f ".env.prod" ]; then
  echo "Error: no existe .env.prod en este directorio."
  exit 1
fi

echo "Cargando variables de entorno desde .env.prod..."
set -a
source .env.prod
set +a

echo "Login en ECR..."
aws ecr get-login-password --region "$REGION" | docker login --username AWS --password-stdin "$ECR_REGISTRY"

echo "Desplegando stack en Docker Swarm..."
docker stack deploy --with-registry-auth -c docker-stack.prod.yml sanos

echo "Servicios desplegados:"
docker service ls