#!/bin/bash

set -e

MANAGER_PRIVATE_IP=$1

if [ -z "$MANAGER_PRIVATE_IP" ]; then
  echo "Uso: ./init-swarm.sh <IP_PRIVADA_MANAGER>"
  exit 1
fi

echo "Inicializando Docker Swarm en manager con IP: $MANAGER_PRIVATE_IP"

docker swarm init --advertise-addr "$MANAGER_PRIVATE_IP"

echo "Comando para unir workers:"
docker swarm join-token worker