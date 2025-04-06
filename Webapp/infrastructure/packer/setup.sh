#!/bin/bash
set -e


echo "Installing dependencies..."

sudo apt-get update -y
sudo apt-get install -y 


sudo DEBIAN_FRONTEND=noninteractive apt-get update -y
sudo DEBIAN_FRONTEND=noninteractive apt-get install -y maven openjdk-21-jdk postgresql postgresql-contrib


# echo "Installing Java JDK .."
# sudo apt-get install -y openjdk-21-jdk


echo "Configuring PostgreSQL..."
# sudo apt-get install -y postgresql postgresql-contrib
sudo systemctl enable postgresql
sudo systemctl start postgresql

# Configure PostgreSQL with proper permissions
echo "Setting up database..."

cd /tmp  # Changing to a directory where postgres user has access

sudo -u postgres psql <<EOF
CREATE DATABASE ${DB_NAME};
ALTER USER $DB_USER WITH PASSWORD '$DB_PASSWORD';
GRANT ALL PRIVILEGES ON DATABASE ${DB_NAME} TO ${DB_USER};
EOF
