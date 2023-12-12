#!/bin/bash

./gradlew clean
./gradlew bootJar
docker-compose build --no-cache
docker-compose up --force-recreate
