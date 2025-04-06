#!/bin/bash

echo "DB_URL=jdbc:postgresql://${aws_db_instance.db_instance.address}:5432/${var.db_name}?createDatabaseIfNotExist=true" >> /etc/environment
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