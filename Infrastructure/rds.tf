

resource "aws_security_group" "database_sg" {
  name        = "database_security_group"
  description = "Allow database traffic from application"
  vpc_id      = aws_vpc.vpc.id


  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "db-security-group"
  }
}

resource "aws_security_group_rule" "db_ingress_from_app" {
  type                     = "ingress"
  from_port                = var.db_port
  to_port                  = var.db_port
  protocol                 = "tcp"
  security_group_id        = aws_security_group.database_sg.id
  source_security_group_id = aws_security_group.app_sg.id
}

resource "aws_db_subnet_group" "db_subnet_group" {
  name       = "csye6225-db-subnet-group"
  subnet_ids = [for subnet in aws_subnet.private_subnets : subnet.id]

  tags = {
    Name = "DB Subnet Group"
  }
}

resource "aws_db_parameter_group" "db_parameter_group" {
  name   = "csye6225-parameter-group"
  family = var.db_parameter_group_family

  tags = {
    Name = "Custom Parameter Group"
  }
}

resource "aws_db_instance" "db_instance" {
  identifier             = "csye6225-2025"
  allocated_storage      = 20
  storage_type           = "gp2"
  engine                 = var.db_engine
  engine_version         = var.db_engine_version
  instance_class         = var.db_instance_class
  db_name                = var.db_name
  username               = var.db_username
  password               = var.db_password
  parameter_group_name   = aws_db_parameter_group.db_parameter_group.name
  db_subnet_group_name   = aws_db_subnet_group.db_subnet_group.name
  vpc_security_group_ids = [aws_security_group.database_sg.id]
  skip_final_snapshot    = true
  publicly_accessible    = false
  multi_az               = false

  tags = {
    Name = "csye6225-db"
  }
}

output "db_hostname" {
  value = aws_db_instance.db_instance.address
}

output "db_port" {
  value = aws_db_instance.db_instance.port
}

output "db_name" {
  value = aws_db_instance.db_instance.db_name
}

output "db_username" {
  value = aws_db_instance.db_instance.username
}