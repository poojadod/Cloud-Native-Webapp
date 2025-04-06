#!/bin/bash
set -e

echo "Creating application user and group..."
sudo groupadd ${APP_GROUP} || true
sudo useradd -m -g ${APP_GROUP} -s /usr/sbin/nologin ${APP_USER} || true

sudo mkdir -p ${APP_DIR}

sudo mv /tmp/webapp-0.0.1-SNAPSHOT.jar ${APP_DIR}/

echo "Setting up application ownership..."
sudo chown -R ${APP_USER}:${APP_GROUP} ${APP_DIR}
sudo chmod -R 750 ${APP_DIR}

