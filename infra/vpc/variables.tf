
/* ============================== */
/* vpc/variables.tf              */
/* ============================== */

variable "vpc_cidr" {
  default     = "10.0.0.0/16"
  description = "CIDR block for the VPC"
}

variable "region" {
  default     = "ap-southeast-2"
  description = "AWS region"
}
