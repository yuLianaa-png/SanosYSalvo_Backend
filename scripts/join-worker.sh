#!/bin/bash

set -e

TOKEN=$1
MANAGER_PRIVATE_IP=$2

if [ -z "$TOKEN" ] || [ -z "$MANAGER_PRIVATE_IP" ]; then
  echo "Uso: ./join-worker.sh <TOKEN> <IP_PRIVADA_MANAGER>"
  exit 1
fi

echo "Uniendo worker al cluster Swarm..."

docker swarm join --token "$TOKEN" "$MANAGER_PRIVATE_IP:2377"