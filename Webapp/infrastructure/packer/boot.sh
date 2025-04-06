#!/bin/bash
set -e

echo "Setting up systemd service..."
sudo mv /tmp/system.service /etc/systemd/system/
sudo chmod 644 /etc/systemd/system/system.service
sudo systemctl daemon-reload
sudo systemctl enable system.service