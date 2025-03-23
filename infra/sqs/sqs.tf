/* ======================= */
/* sqs/sqs.tf - SQS Queues */
/* ======================= */

resource "aws_sqs_queue" "transaction" {
  name                      = var.transaction_queue_name
  delay_seconds             = 0
  max_message_size          = 262144
  message_retention_seconds = 345600
  receive_wait_time_seconds = 20  # Enable long polling
  visibility_timeout_seconds = 30
}

resource "aws_sqs_queue" "fraud_alert" {
  name                      = var.fraud_alert_queue_name
  delay_seconds             = 0
  max_message_size          = 262144
  message_retention_seconds = 345600
  receive_wait_time_seconds = 20  # Enable long polling
  visibility_timeout_seconds = 30
}
