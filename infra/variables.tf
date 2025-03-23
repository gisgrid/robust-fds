/* ======================= */
/* variables.tf - Variables */
/* ======================= */
variable "region" {
  description = "The AWS region to deploy resources."
  type        = string
  default     = "<aws_region>"
}

variable "cluster_name" {
  description = "Name of the EKS cluster."
  type        = string
  default     = "fraud-detection-cluster"
}

variable "transaction_queue_name" {
  default     = "transaction-queue"
  description = "Main queue for incoming transactions"
}

variable "fraud_alert_queue_name" {
  default     = "fraud-alert-queue"
  description = "Queue for fraud alerts"
}


variable "ecr_repo_name" {
  description = "Name of the ECR repository."
  type        = string
  default     = "fraud-detection-ecr"
}