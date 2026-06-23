#!/bin/bash

set -e

echo "Escalando servicios principales..."

docker service scale sanos_pet-service=3
docker service scale sanos_match-service=3

echo "Estado de servicios:"
docker service ls