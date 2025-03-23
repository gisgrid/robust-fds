# 🛠️ Infrastructure Setup

This folder contains Terraform code to deploy the infrastructure for the Fraud Detection Service (FDS) on AWS.

## 📦 Components

The Terraform configuration includes resources such as:

- Amazon EKS Cluster (Elastic Kubernetes Service)
- Amazon ECR Repository (Elastic Container Registry)
- Amazon SQS (Simple Queue Service)
- IAM Roles and Policies
- CloudWatch Log Groups
- (Optional) SNS Topic for Alerts

## 🚀 Getting Started

### 1. Install Dependencies

- [Terraform](https://developer.hashicorp.com/terraform/downloads)
- [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2.html)
- Configure AWS credentials:
  ```bash
  aws configure
  ```

### 2. Initialize Terraform

```bash
terraform init
```

### 3. Review and Apply

```bash
terraform plan
terraform apply
```

> 🔐 Never commit `.tfstate`, `.terraform`, or sensitive files like `terraform.tfvars` to version control.

---

## ⚙️ Required Changes

Before using this code, **you must update the following placeholders** in `.tf` files:

- Change: `<aws_region>` → your AWS region (e.g., `ap-southeast-2`)
- Change: `<aws_account_id>` → your 12-digit AWS account ID

You may also need to update:

- VPC/Subnet IDs if you're not creating them here
- EKS node group configuration (instance type, size, etc.)
- ECR repository name and Docker image tag

---

## 📁 File Structure

```
infra/
├── main.tf
├── variables.tf
├── outputs.tf
├── versions.tf
└── README.md
```

---

## 📝 Notes

- Use `terraform fmt` to format your code
- Use `terraform validate` to catch syntax errors
- Use `terraform workspace` if deploying to multiple environments (dev, staging, prod)

---

## 📜 License

This project is licensed under the MIT License.
