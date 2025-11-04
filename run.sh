#!/bin/bash
set -e
cd "$(dirname "$0")"
mvn -q -DskipTests package
exec java -jar target/prom-java-metrics-0.1.0-jar-with-dependencies.jar
