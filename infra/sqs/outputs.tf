
/* ========================= */
/* sqs/outputs.tf - Outputs */
/* ========================= */

output "transaction_queue_url" {
  value       = aws_sqs_queue.transaction.id
  description = "URL of the transaction SQS queue"
}

output "transaction_queue_arn" {
  value       = aws_sqs_queue.transaction.arn
  description = "ARN of the transaction SQS queue"
}

output "fraud_alert_queue_url" {
  value       = aws_sqs_queue.fraud_alert.id
  description = "URL of the fraud alert SQS queue"
}

output "fraud_alert_queue_arn" {
  value       = aws_sqs_queue.fraud_alert.arn
  description = "ARN of the fraud alert SQS queue"
}
