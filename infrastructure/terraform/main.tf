/*terraform {
    required_providers {
      aws = {
        source = "hashicrp/aws"
        version = "~> 5.0"
      }
    }    
}

*/
// Configure AWS provider block
provider "aws" {
    profile = "fred"
    region = "us-east-2"
}

// Create a VPC
resource "aws_vpc" "qr_code_vpc" {
    cidr_block = "10.0.0.0/16"

    tags = {
       Name = var.vpc_name  
    }
}

resource "aws_security_group" "qr_code_sg" {
  name        = "HTTP and SSH"
  description = "Allow inbound HTTP and SSH traffic from my IP"

  // Ingress rule for HTTP
  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["${var.my_ip}/32"]
  }

  // Ingress rule for SSH
  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["${var.my_ip}/32"]  
  }

  // Egress rule (allow all outbound traffic)
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}


// Resource block
resource "aws_instance" "qr_code_instance" {
    ami             = var.ec2_ami
    instance_type   = var.ec2_instance_type
    vpc_security_group_ids = [aws_security_group.qr_code_sg.id]
    associate_public_ip_address = true 
    key_name        = "fredKey" 

    tags = {
        Name = var.instance_tag_name
    }
}
