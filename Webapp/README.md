## Assignment 4. Building Custom Application Images using Packer

This Assignment contains the Packer template to create a custom Amazon Machine Image (AMI) and a Google Compute Image with the application and dependencies pre-installed.

## Prerequisites

Ensure you have the following installed before proceeding:
	•	Packer (Latest Version)
	•	AWS CLI (Configured with proper credentials)
	•	GCP CLI (Configured with the necessary IAM roles)
	•	GitHub Actions (Configured in the repository for CI/CD)

## Features

The Packer template does the following:
	1.	Uses Ubuntu 24.04 LTS as the base image.
	2.	Installs MySQL/MariaDB/PostgreSQL locally.
	3.	Creates a non-login user (csye6225) with the primary group csye6225.
	4.	Copies the application binary and configuration files into the image.
	5.	Installs all dependencies required to run the application.
	6.	Adds a systemd service to ensure the application starts on instance launch.
	7.	Builds the custom AMI (AWS) and GCE image (GCP) in parallel.


### Building the Image

To manually build the image, run the following commands:

Validate the Packer Template
>> packer fmt .
>> packer validate .

Build the Custom Image
>> packer build .

GitHub Actions Workflow
	1.	On Pull Request:
	•	Runs packer fmt (Fails if formatting is incorrect).
	•	Runs packer validate (Fails if validation does not pass).

	2.	On Merge to Main Branch:
	•	Runs integration tests.
	•	Builds the application artifact.
	•	Builds the custom AMI (AWS) and GCE image (GCP).
	•	Ensures systemd service is set up properly.
	•	Prevents building an image if any step fails.

## Assignment 2. Automating Application Setup with Shell Script


This assignment contains a shell script to automate the setup of a Spring Boot application with PostgreSQL on Ubuntu 24.04 LTS.

## Prerequisites

- Ubuntu 24.04 LTS
- Spring Boot application ZIP file

## Features

- Updates package lists and upgrades system packages.

- Installs PostgreSQL as the RDBMS.

- Creates a database in PostgreSQL.

- reates a new Linux group and user for the application.

- Extracts the application into /opt/csye6225/.

- Updates permissions for the application directory.

- Installs dependencies for spring-boot application
  

## Instructions


Clone the repository:

     > git clone <repository_url>

Copy the script and zip file of the web-app repo to the server 

Make the script executable:

    chmod +x setup.sh

Run the script:

    ./setup.sh






## Assignment 1: Cloud Native Web Application

This is a cloud-native web application built using Spring Boot and PostgreSQL. It includes a health check API that monitors the application's ability to handle requests by inserting records into a database.


## Features

### Healthz Endpoint

- The API includes a healthz endpoint designed to perform a database connection test
  
- To start database server use Postgres desktop app
  
  or Alternaltively, 
  
      cd /Library/PostgreSQL/17/bin

      # Start PostgreSQL
       sudo -u postgres /Library/PostgreSQL/17/bin/pg_ctl start -D /Library/PostgreSQL/17/data

 
      # Stop PostgreSQL
       sudo -u postgres /Library/PostgreSQL/17/bin/pg_ctl stop -D /Library/PostgreSQL/17/data
 

- To verify the connection status, you can use the following curl request:

      curl -vvvv http://localhost:8080/healthz
- This request returns either "OK"(2002) or "Service Unavailable"(503) based on the connection status.


### Middleware Blocking Other HTTP Methods

- The healthz endpoint has been secured by middleware to allow only specific HTTP methods.

- To test this middleware, you can use the following curl requests:
   - PUT request:
      
      curl -vvvv -X PUT http://localhost:8080/healthz
   
   - POST request:
     
     curl -vvvv -X PUT http://localhost:8080/healthz
   
   - DELETE request:
   
      curl -vvvv -X DELETE http://localhost:8080/healthz
   
   - PATCH request:
      
      curl -vvvv -X PATCH http://localhost:8080/healthz


### Technologies Used

Backend: Spring Boot (Java)

Database: PostgreSQL

ORM: Hibernate

Environment Management: .env file with dotenv-java

### Prerequisites
Before building and deploying the application locally, ensure you have the following installed:

1. Java 17: Ensure Java 17 or higher is installed.

2. PostgreSQL: Install and set up PostgreSQL.

3. Maven: Ensure Maven 3.6 or higher is installed for building the project.

4. Environment Variables
   Create a .env file in the root of the project with the following content:

   `DB_URL=jdbc:postgresql://localhost:5433/<Database>`

   `DB_USERNAME=<your-username>`

   `DB_PASSWORD=<your_password>`

Replace the above variables with your credentials. 

### Build and Deploy Instructions

1. Clone the Repository

   >`git clone <repository-url>`
   
   >`cd webapp_fork`


2. And Build and Run the Application "WebApplication.java"





