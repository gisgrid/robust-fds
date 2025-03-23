/* ============================ */
/* monitoring/variables.tf - Module Variables */
/* ============================ */
variable "cluster_name" {
  description = "EKS cluster name for tagging metrics/dimensions."
  type        = string
}
