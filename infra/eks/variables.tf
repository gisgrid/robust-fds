/* ======================= */
/* eks/variables.tf - Module Variables */
/* ======================= */
variable "cluster_name" {
  description = "Name of the EKS cluster."
  type        = string
}

variable "subnet_ids" {
  description = "List of subnet IDs for the EKS cluster."
  type        = list(string)
}

variable "region" {
  description = "AWS region to deploy to."
  type        = string
}