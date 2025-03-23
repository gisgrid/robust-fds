
/* =========================== */
/* sqs/variables.tf - Variables */
/* =========================== */

variable "transaction_queue_name" {
  description = "Name of the main transaction queue"
  default     = "transaction-queue"
}

variable "fraud_alert_queue_name" {
  description = "Name of the fraud alert queue"
  default     = "fraud-alert-queue"
}
