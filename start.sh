#!/bin/bash

./gradlew clean
./gradlew bootJar
java -jar build/libs/task-app-0.0.1-SNAPSHOT.jar
