#!/bin/bash
set -e

sudo touch /opt/webapp/application.properties

echo "spring.datasource.url=jdbc:postgresql://localhost:5432/${DB_NAME}?createDatabaseIfNotExist=true" | sudo tee -a /opt/webapp/application.properties
echo "spring.datasource.username=${DB_USER}" | sudo tee -a /opt/webapp/application.properties
echo "spring.datasource.password=${DB_PASSWORD}" | sudo tee -a /opt/webapp/application.properties
echo "spring.datasource.hikari.connection-timeout=3000" | sudo tee -a /opt/webapp/application.properties
echo "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect" | sudo tee -a /opt/webapp/application.properties
echo "spring.jpa.hibernate.ddl-auto=update" | sudo tee -a /opt/webapp/application.properties
echo "spring.datasource.driver-class-name=org.postgresql.Driver" | sudo tee -a /opt/webapp/application.properties

sudo chown csye6225:csye6225 /opt/webapp/application.properties
