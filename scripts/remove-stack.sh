#!/bin/bash

set -e

echo "Eliminando stack sanos..."

docker stack rm sanos

echo "Stack eliminado."