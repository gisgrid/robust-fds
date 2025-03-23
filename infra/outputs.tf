/* ======================= */
/* outputs.tf - Output Values */
/* ======================= */
output "eks_endpoint" {
  description = "EKS cluster API endpoint."
  value       = module.eks.cluster_endpoint
}

output "transaction_queue_url" {
  value = module.sqs.transaction_queue_url
}

output "fraud_alert_queue_url" {
  value = module.sqs.fraud_alert_queue_url
}

output "ecr_repository_url" {
  description = "URL of the created ECR repository."
  value       = module.ecr.repository_url
}

