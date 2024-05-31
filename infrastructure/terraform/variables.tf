variable "vpc_name" {
  type        = string
  default     = "QRCodeVpc"
  description = "The name of the VPC"
}


variable "my_ip" {
  type        = string
  default     = "136.32.252.25"
  description = "My IP address"
}


variable "instance_tag_name" {
  type        = string
  default     = "myQrcode"
  description = "Tag for instace name"
}

variable "ec2_instance_type" {
    type      = string
    default   = "t2.micro"
    description = "EC2 type"
}

variable "ec2_ami" {
  type        = string
  default     = "ami-0900fe555666598a2"
  description = "Value of AMI ID for EC2 instance"
}
