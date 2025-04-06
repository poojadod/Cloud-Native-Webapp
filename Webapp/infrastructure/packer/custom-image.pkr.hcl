packer {
  required_plugins {
    amazon = {
      version = ">= 1.2.6"
      source  = "github.com/hashicorp/amazon"
    }
    # googlecompute = {
    #   version = ">= 1.1.3"
    #   source  = "github.com/hashicorp/googlecompute"
    # }
  }
}

# # GCP Custom Image Configuration
# source "googlecompute" "custom_image" {
#   project_id              = var.project_id
#   source_image_family     = var.source_image_family
#   image_name              = "${var.image_name}-${formatdate("YYYYMMDDHHmmss", timestamp())}"
#   zone                    = var.zone
#   ssh_username            = var.ssh_username
#   image_family            = var.image_family
#   image_storage_locations = [var.image_storage_locations]
#   image_description       = var.image_description
#   communicator            = var.communicator
#   disk_type               = var.disk_type
# }

# AWS Source Configuration
source "amazon-ebs" "custom_image" {
  ami_name      = "${var.image_name}-${formatdate("YYYYMMDDHHmmss", timestamp())}"
  region        = var.aws_region
  instance_type = var.aws_instance_type
  source_ami    = var.aws_source_ami
  ssh_username  = var.ssh_username

  ami_users = [var.aws_demo_account_id]

}

build {
  name = "webapp-image"

  sources = [
    "source.amazon-ebs.custom_image",
    # "source.googlecompute.custom_image"
  ]

  # Copy files first
  provisioner "file" {
    source      = var.artifact_src_path
    destination = "/tmp/webapp-0.0.1-SNAPSHOT.jar"

  }

  provisioner "file" {
    source      = var.systemd_service_src_path
    destination = "/tmp/system.service"
  }

  # Install Java
  provisioner "shell" {
    inline = [
      "sudo apt update",
      "sudo apt install -y openjdk-21-jdk",
    ]
  }

  # User setup, group and user 
  provisioner "shell" {
    environment_vars = [
      "APP_USER=${var.app_user}",
      "APP_GROUP=${var.app_group}",
      "APP_DIR=${var.app_dir}"
    ]
    script = "${path.root}/usergroup.sh"
  }

  # provisioner "shell" {
  #   environment_vars = [
  #     "DB_USER=${var.db_user}",
  #     "DB_PASSWORD=${var.db_password}",
  #     "DB_NAME=${var.db_name}"
  #   ]
  #   script = "${path.root}/properties.sh"

  # }

  provisioner "file" {
    source      = "amazon-cloudwatch-agent.json"
    destination = "/tmp/amazon-cloudwatch-agent.json"
  }

  provisioner "shell" {
    inline = [
      "sudo apt update",

      "wget https://s3.amazonaws.com/amazoncloudwatch-agent/ubuntu/amd64/latest/amazon-cloudwatch-agent.deb",
      "sudo dpkg -i -E amazon-cloudwatch-agent.deb",
      "rm amazon-cloudwatch-agent.deb",

      "sudo mkdir -p /opt/aws/amazon-cloudwatch-agent/etc",
      "sudo cp /tmp/amazon-cloudwatch-agent.json /opt/aws/amazon-cloudwatch-agent/etc/amazon-cloudwatch-agent.json",
      "sudo chown root:root /opt/aws/amazon-cloudwatch-agent/etc/amazon-cloudwatch-agent.json",
      "sudo chmod 644 /opt/aws/amazon-cloudwatch-agent/etc/amazon-cloudwatch-agent.json",
      "sudo systemctl enable amazon-cloudwatch-agent"
    ]
  }




  # Final setup
  provisioner "shell" {
    environment_vars = [
      "APP_USER=${var.app_user}",
      "APP_GROUP=${var.app_group}",
      "APP_DIR=${var.app_dir}"
    ]
    script = "boot.sh"
  }

}