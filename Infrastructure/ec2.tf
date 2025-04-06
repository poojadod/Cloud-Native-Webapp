# Creating Security Group for Web Application

resource "aws_security_group" "app_sg" {
  name        = "app_security_group"
  description = "Allow web traffic and SSH"
  vpc_id      = aws_vpc.vpc.id

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"] # Allows SSH access 
  }

  # ingress {
  #   from_port   = 80
  #   to_port     = 80
  #   protocol    = "tcp"
  #   cidr_blocks = ["0.0.0.0/0"] # Allow HTTP traffic
  # }

  # ingress {
  #   from_port   = 443
  #   to_port     = 443
  #   protocol    = "tcp"
  #   cidr_blocks = ["0.0.0.0/0"] # Allow HTTPS traffic
  # }

  ingress {
    from_port       = 8080 # Web Application port 
    to_port         = 8080
    protocol        = "tcp"
    security_groups = [aws_security_group.lb_sg.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# #Instance Profile for EC2
# resource "aws_iam_instance_profile" "ec2_profile" {
#   name = "ec2-instance-profile"
#   role = aws_iam_role.ec2_role.name
# }

# Creating EC2 Instance in Public Subnet
# resource "aws_instance" "app_instance" {
#   ami                         = var.aws_ami_id
#   instance_type               = var.aws_instance_type
#   vpc_security_group_ids      = [aws_security_group.app_sg.id]
#   subnet_id                   = aws_subnet.public_subnets[0].id #using the first public subnet for now
#   associate_public_ip_address = true                            # Ensure it gets a public IP
#   key_name                    = var.aws_key_name

#   iam_instance_profile = aws_iam_instance_profile.ec2_s3_access_profile.name

#   root_block_device {
#     volume_size           = var.aws_volume_size
#     volume_type           = var.aws_volume_type
#     delete_on_termination = true
#   }



#   user_data = <<-EOF
#               #!/bin/bash
#                 # Environment variables for application
#                 echo "DB_URL=jdbc:postgresql://${aws_db_instance.db_instance.address}:5432/${var.db_name}?createDatabaseIfNotExist=true" >> /etc/environment
#                 echo "DB_USERNAME=${var.db_username}" >> /etc/environment
#                 echo "DB_PASSWORD=${var.db_password}" >> /etc/environment
#                 echo "AWS_REGION=${var.aws_region}" >> /etc/environment
#                 echo "S3_BUCKET_NAME=${aws_s3_bucket.webapp_bucket.bucket}" >> /etc/environment

#                 # Create log directory with proper permissions
#                 sudo mkdir -p /var/log/webapp/
#                 sudo touch /var/log/webapp/application.log
#                 sudo chown csye6225:csye6225 /var/log/webapp/application.log
#                 sudo chmod 755 /var/log/webapp

#                 # Configure and restart CloudWatch Agent
#                 sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl \
#                   -a fetch-config \
#                   -m ec2 \
#                   -c file:/opt/aws/amazon-cloudwatch-agent/etc/amazon-cloudwatch-agent.json \
#                   -s

#                 sudo systemctl restart amazon-cloudwatch-agent

#                 # Start the application
#                 sudo systemctl restart system.service

#                 EOF 



#   tags = {
#     Name = "WebAppInstance"
#   }
# }