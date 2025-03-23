
/* ============================== */
/* k8s-resources/variables.tf    */
/* ============================== */

variable "app_name" {
  default = "fraud-detection-app"
}

variable "namespace" {
  default = "default"
}

variable "image_url" {
  description = "Docker image URL from ECR"
  type        = string
}

variable "transaction_queue_url" {
  description = "URL of the transaction SQS queue"
  type        = string
}

variable "fraud_alert_queue_url" {
  description = "URL of the fraud alert SQS queue"
  type        = string
}

variable "threshold_amount" {
  description = "Amount threshold to trigger fraud alert"
  type        = string
  default     = "1000"
}

variable "replicas" {
  default = 4
}

variable "min_replicas" {
  default = 2
}

variable "max_replicas" {
  default = 6
}

variable "target_cpu_utilization" {
  default = 70
}
