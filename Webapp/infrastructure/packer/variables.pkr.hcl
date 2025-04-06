# Common Variables
variable "image_name" {
  type        = string
  description = "Name of the image to be created"
  default     = "webapp"
}

# AWS Variables
variable "aws_region" {
  type        = string
  description = "AWS region"
  default     = "us-east-1"
}

variable "aws_instance_type" {
  type        = string
  description = "AWS instance type"
  default     = "t2.micro"
}

variable "aws_source_ami_owner" {
  type        = string
  description = "Owner ID of the source AMI"
  default     = "099720109477"
}

variable "aws_source_ami_filter_name" {
  type        = string
  description = "Name filter for source AMI"
  default     = "ubuntu/images/hvm-ssd/ubuntu-jammy-22.04-amd64-server-*"
}

# Application Variables
variable "ssh_username" {
  type        = string
  description = "SSH username for the instances"
  default     = "ubuntu"
}

variable "app_user" {
  type        = string
  description = "Application user"
  default     = "csye6225"
}

variable "app_group" {
  type        = string
  description = "Application group"
  default     = "csye6225"
}

variable "app_dir" {
  type        = string
  description = "Application directory"
  default     = "/opt/webapp"
}

variable "db_name" {
  type        = string
  description = "Database name"
  default     = "csye6225"

}

variable "db_user" {
  type        = string
  description = "Database user"
  default     = "postgres"

}

variable "db_password" {
  type        = string
  description = "Database password"
  sensitive   = true
  default     = "1008"

}

variable "artifact_src_path" {
  type        = string
  description = "Source path for the application artifact"
  default     = "../../target/webapp-0.0.1-SNAPSHOT.jar"
}

variable "artifact_dest_path" {
  type        = string
  description = "Destination path for the application artifact"
  default     = "/tmp/web-app.jar"
}

variable "systemd_service_src_path" {
  type        = string
  description = "Path to the systemd service file"
  default     = "system.service"
}

variable "systemd_service_dest_name" {
  type        = string
  description = "Destination name for the systemd service file"
  default     = "/tmp/system.service"
}

# GCP Variables

variable "project_id" {
  type        = string
  default     = "csye6225-dev-452000"
  description = "The GCP project ID"
}

variable "source_image_family" {
  type        = string
  default     = "ubuntu-2404-lts-amd64"
  description = "The source image family for the VM"
}

variable "zone" {
  type        = string
  default     = "us-east1-b"
  description = "The zone where the VM will be created"
}

variable "image_family" {
  type        = string
  default     = "gcp-dev"
  description = "The image family for the created image"
}

variable "image_storage_locations" {
  type        = string
  default     = "us-east1"
  description = "The storage location for the created image"
}

variable "image_description" {
  type        = string
  default     = "Custom image for CSYE6255 Cloud Computing"
  description = "Description for the created image"
}

variable "communicator" {
  type        = string
  default     = "ssh"
  description = "The communicator to use for provisioning"
}

variable "disk_type" {
  type        = string
  default     = "pd-standard"
  description = "The disk type for the VM"
}

variable aws_source_ami {
  type        = string
  description = "The source AMI for the image"
  default     = "ami-04b4f1a9cf54c11d0"
}

variable aws_demo_account_id {
  type        = string
  description = "The account ID for the demo account"
  default     = "396913723256"
}