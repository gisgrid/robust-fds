/* ======================= */
/* backend.tf - Remote State */
/* ======================= */
terraform {
  backend "s3" {
    bucket         = "<aws-terraform-state-bucket>"
    key            = "fraud-detection/terraform.tfstate"
    region         = "<aws_region>"
    use_lockfile   = true
    encrypt        = true
  }
}
