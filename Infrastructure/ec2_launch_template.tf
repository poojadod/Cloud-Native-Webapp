resource "aws_launch_template" "asg_template" {
  name_prefix   = "csye6225_asg"
  image_id      = var.aws_ami_id
  instance_type = var.aws_instance_type
  key_name      = var.aws_key_name

  network_interfaces {
    associate_public_ip_address = true
    security_groups             = [aws_security_group.app_sg.id]
  }

  iam_instance_profile {
    name = aws_iam_instance_profile.ec2_s3_access_profile.name
  }

  user_data = base64encode(<<-EOF
                #!/bin/bash
                # Environment variables for application
                echo "DB_URL=jdbc:postgresql://${aws_db_instance.db_instance.address}:5432/${var.db_name}?createDatabaseIfNotExist=true" >> /etc/environment
                echo "DB_USERNAME=${var.db_username}" >> /etc/environment
                echo "DB_PASSWORD=${var.db_password}" >> /etc/environment
                echo "AWS_REGION=${var.aws_region}" >> /etc/environment
                echo "S3_BUCKET_NAME=${aws_s3_bucket.webapp_bucket.bucket}" >> /etc/environment

                # Create log directory with proper permissions
                sudo mkdir -p /var/log/webapp/
                sudo touch /var/log/webapp/application.log
                sudo chown csye6225:csye6225 /var/log/webapp/application.log
                sudo chmod 755 /var/log/webapp

                # Configure and restart CloudWatch Agent
                sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl \
                  -a fetch-config \
                  -m ec2 \
                  -c file:/opt/aws/amazon-cloudwatch-agent/etc/amazon-cloudwatch-agent.json \
                  -s

                sudo systemctl restart amazon-cloudwatch-agent

                # Start the application
                sudo systemctl restart system.service

                EOF 
  )

  tag_specifications {
    resource_type = "instance"
    tags = {
      Name = "WebAppAutoInstance"
    }
  }
}

