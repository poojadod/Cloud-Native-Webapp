variable "aws_region" {
  description = "AWS region to deploy resources"
  type        = string
  default     = "us-east-1"
}

variable "aws_profile" {
  description = "AWS CLI profile to use"
  type        = string
  default     = "dev"
}

variable "vpc_cidr" {
  description = "CIDR block for the VPC"
  type        = string
  default     = "10.0.0.0/16"
}

variable "public_subnets" {
  description = "List of public subnet CIDRs"
  type        = list(string)
  default     = ["10.0.1.0/24", "10.0.2.0/24", "10.0.3.0/24"]
}

variable "private_subnets" {
  description = "List of private subnet CIDRs"
  type        = list(string)
  default     = ["10.0.4.0/24", "10.0.5.0/24", "10.0.6.0/24"]
}

variable "availability_zones" {
  description = "List of availability zones"
  type        = list(string)
  default     = ["us-east-1a", "us-east-1b", "us-east-1c"]
}


variable "vpc_name" {
  description = "Name of the VPC"
  type        = string
  default     = "vpc-a1"
}

variable "igw_name" {
  description = "Name of the IGW"
  type        = string
  default     = "igw-a1"
}

variable "public_subnet_name" {
  description = "Base name for public subnets"
  type        = string
  default     = "public-subnet"
}


variable "private_subnet_name" {
  description = "Base name for public subnets"
  type        = string
  default     = "private-subnet"
}

variable "public_route_table_name" {
  description = "Name for the public route table"
  type        = string
  default     = "public-route-table"
}

variable "private_route_table_name" {
  description = "Name for the public route table"
  type        = string
  default     = "private-route-table"
}

variable "aws_ami_id" {
  description = "AMI ID for the EC2 instances"
  type        = string
  default     = "ami-0f4dbfbbe1b0ba271"


}
variable "aws_instance_type" {
  description = "Instance type for the EC2 instances"
  type        = string
  default     = "t2.micro"

}

variable "aws_volume_size" {
  description = "Volume size for the EC2 instances"
  type        = number
  default     = 25
}

variable "aws_volume_type" {
  description = "Volume type for the EC2 instances"
  type        = string
  default     = "gp2"

}

variable "aws_key_name" {
  description = "Key pair name for the EC2 instances"
  type        = string
  default     = "dev-key-pair"

}


variable "db_port" {
  description = "Port for the RDS instance"
  type        = number
  default     = 5432
}

variable "db_username" {
  description = "Username for the RDS instance"
  type        = string
  default     = "postgres"
}

variable "db_password" {
  description = "Password for the RDS instance"
  type        = string
  default     = "password1008"
}

variable "db_name" {
  description = "Database name for the RDS instance"
  type        = string
  default     = "csye6225"
}

variable "db_parameter_group_family" {
  description = "Parameter group family for the RDS instance"
  type        = string
  default     = "postgres14"

}

variable "db_engine" {
  description = "Database engine for the RDS instance"
  type        = string
  default     = "postgres"

}



variable "db_instance_class" {
  description = "Instance class for the RDS instance"
  type        = string
  default     = "db.t3.micro"

}

variable "db_subnet_group_name" {
  description = "Name of the DB subnet group"
  type        = string
  default     = "csye6225-db-subnet-group"

}

variable "db_parameter_group_name" {
  description = "Name of the DB parameter group"
  type        = string
  default     = "csye6225-parameter-group"

}

variable "db_instance_name" {
  description = "Name of the RDS instance"
  type        = string
  default     = "csye6225-db"

}

variable "aws_s3_bucket_name" {
  description = "Name of the S3 bucket"
  type        = string
  default     = "csye6225-s3-bucket"

}

variable "db_engine_version" {
  description = "Database engine version for the RDS instance"
  type        = string
  default     = "14.15"

}


variable "route53_zone_id" {
  description = "Route 53 Hosted Zone ID"
  type        = string
  default     = "Z09762131B4Z1PII8W6M0"
}

variable "domain_name" {
  description = "Domain name for Route 53"
  type        = string
  default     = "poojadoddannavar.me"

}
