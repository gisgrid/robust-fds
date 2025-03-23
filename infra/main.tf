/* ======================= */
/* main.tf - Modules Setup */
/* ======================= */
module "eks" {
  source     = "./eks"
  cluster_name = var.cluster_name
  subnet_ids = module.vpc.private_subnet_ids
  region       = var.region
}

module "sqs" {
  source = "./sqs"
  transaction_queue_name = var.transaction_queue_name
  fraud_alert_queue_name = var.fraud_alert_queue_name
}

module "ecr" {
  source = "./ecr"
  repo_name = var.ecr_repo_name
}

module "monitoring" {
  source = "./monitoring"
  cluster_name = module.eks.cluster_name
}

module "k8s_resources" {
  source = "./k8s-resources"

  app_name                = "fraud-detection-app"
  image_url 			= "<aws_account_id>.dkr.ecr.<aws_region>.amazonaws.com/fraud-detection-ecr:latest"
  transaction_queue_url  = module.sqs.transaction_queue_url
  fraud_alert_queue_url  = module.sqs.fraud_alert_queue_url
  threshold_amount       = "1000"

  namespace              = "fraud-detection"
  replicas               = 4
  min_replicas           = 2
  max_replicas           = 6
  target_cpu_utilization = 70
}

module "vpc" {
  source   = "./vpc"
  region   = var.region
  vpc_cidr = "10.0.0.0/16"
}

