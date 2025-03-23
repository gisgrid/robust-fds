/* ======================= */
/* eks/outputs.tf - EKS Outputs */
/* ======================= */
output "cluster_endpoint" {
  description = "EKS cluster endpoint URL."
  value       = aws_eks_cluster.this.endpoint
}

output "cluster_name" {
  value = var.cluster_name
}

output "cluster_certificate_authority_data" {
  value = aws_eks_cluster.this.certificate_authority[0].data
}

output "oidc_provider_url" {
  value = aws_iam_openid_connect_provider.this.url
}

output "oidc_provider_arn" {
  value = aws_iam_openid_connect_provider.this.arn
}
