#!/bin/bash

# Update the package list and upgrade the packages
sudo apt update -y
sudo apt upgrade -y
sudo apt install unzip -y

# Create a new Linux group for the application
echo "Creating new group 'csye6225_group'"
sudo groupadd -f csye6225_group


# Create a new user for the application and check if the user already exists
echo "Creating new user 'csye6225_user'"
if id "csye6225_user" &>/dev/null; then
   echo "User 'csye6225_user' already exists."
else
   sudo useradd -m -s /bin/bash -g csye6225_group csye6225_user
fi


# Unzip the application in /opt/csye6225 directory
echo "Unzipping application to /opt/csye6225..."
sudo mkdir -p /opt/csye6225
sudo unzip -o /home/Pooja_Doddannavar_002059406_02.zip -d /opt/csye6225


# Update permissions of the folder and files

echo "Updating permissions..."
sudo chown -R csye6225_user:csye6225_group /opt/csye6225
sudo chmod -R 750 /opt/csye6225
source .env
export DB_URL=$DB_URL
export DB_USERNAME=$DB_USERNAME
export DB_NAME=$DB_NAME
export DB_PASSWORD=$DB_PASSWORD
DB_USER=$DB_USERNAME

# Install PostgreSQL
sudo apt install postgresql postgresql-contrib -y 
sudo systemctl status postgresql
sudo systemctl start postgresql
sudo -u postgres psql <<EOF
CREATE USER $DB_USER WITH PASSWORD '$DB_PASSWORD';
ALTER USER $DB_USER CREATEDB CREATEROLE;
CREATE DATABASE $DB_NAME;
GRANT ALL PRIVILEGES ON DATABASE $DB_NAME TO $DB_USER;
GRANT ALL ON SCHEMA public TO $DB_USER;
ALTER USER $DB_USER WITH PASSWORD '${DB_PASSWORD}';
EOF


# Install Java and Maven 

sudo apt install -y openjdk-21-jdk
sudo apt install maven -y

