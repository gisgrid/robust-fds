# Robust-FDS: Real-Time Fraud Detection System
A real-time fraud detection system framework (with Java, AWS EKS, Terraform).

## 📚 Table of Contents

- [1. Project Overview](#1-project-overview)
- [2. Main Components](#2-main-components)
- [3. How to Run & Reproduce](#3-how-to-run--reproduce)
- [4. Contact Information](#4-contact-information)

---

## 1. Project Overview

**Objective**:  
Develop a real-time fraud detection system in Java, deploy it on Kubernetes (AWS EKS), and detect and handle fraudulent transactions swiftly and accurately.

**Key Requirements**:

### 🧠 Core Functionality
- Analyze financial transactions in real-time to detect potential fraud.
- Use a rule-based detection mechanism (e.g., transaction threshold, suspicious accounts).
- Log and alert when fraudulent transactions are detected.

### 🔁 High Availability and Resilience
- Deploy on AWS EKS using Kubernetes.
- Use Kubernetes features like Deployment, Service, and HPA for high availability.
- Utilize AWS SQS to queue and handle incoming transactions.

### ⚡ Performance and Observability
- Achieve real-time, low-latency transaction processing by combining long polling with a batch pull mechanism, enabling efficient message consumption and reduced API overhead.
- Implement distributed logging using AWS CloudWatch.

---

## 2. Main Components

### 🧩 2.1 Application Source Code (`/app`)
- Java 17 application source code (under `src/`), using Apache Maven and Amazon Corretto JDK.
- Includes `pom.xml` and `Dockerfile`.
- Kubernetes manifests under `app/k8s/`:
  - `deployment.yaml`
  - `service.yaml`
  - `configmap.yaml`

### ⚙️ 2.2 Infrastructure as Code (`/infra`)
- Terraform-based AWS infrastructure setup.
- Refer to `infra/infra-README.md` for setup instructions and best practices.
- Modules include:
  - ECR, EKS, K8s resources
  - SQS, CloudWatch, VPC
  - IRSA and permissions configuration
- Terraform files:
  - `main.tf`, `variables.tf`, `outputs.tf`, and more

### 📄 2.3 Documentation (`/docs`)
- `README.md` (this file)
- [`docs/architecture.md`](docs/architecture.md) – system architecture description
  - High-level architecture & diagrams
  - EKS deployment and resilience explanation
  - Data flow diagram
- [`test/test-report.md`](test/test-report.md) – detailed test report
- [`infra/infra-README.md`](infra/infra-README.md) – infrastructure setup guide

### ✅ 2.4 Test Report (`test/test-report.md`)

- [`test/test-report.md`](test/test-report.md) – detailed test report

- **Business Logic Simulation** using normal & fraud scenarios
- **Unit Testing** using JUnit
- **Integration Testing** to verify SQS and CloudWatch integration
- **Resilience Testing** for pod restarts, node failure recovery

---

## 3. How to Run & Reproduce

### 🏗 3.1 Deploy Infrastructure (Terraform)
- Includes everything needed to bootstrap cloud infrastructure on AWS.
- Configure AWS CLI credentials.
- See [`infra/infra-README.md`](infra/infra-README.md) for:
  - Dependency installation
  - Placeholder replacement (e.g., `<aws_region>`, `<aws_account_id>`)
  - Terraform plan/apply steps

### 🚀 3.2 Build & Push Docker Image
- Java version: **Amazon Corretto JDK 17**
- Build tool: **Apache Maven 3.9.9**
- Docker version: **27.5.1**
- Build and push:
  ```bash
  mvn clean install
  docker build -t <your-ecr-repo>:latest .
  aws ecr get-login-password | docker login --username AWS --password-stdin <your-aws-account>.dkr.ecr.<region>.amazonaws.com
  docker push <your-ecr-repo>:latest
  ```

### 🧪 3.3 Testing & Validation (LocalStack)
- Local development and unit testing use **LocalStack** to simulate AWS services:
  - SQS, CloudWatch, etc.
- Run tests:
  ```bash
  mvn test
  ```
- Test logic is under `src/test/`
- See [`test/test-report.md`](test/test-report.md) for full details.

### ☁️ 3.4 Reproduce on AWS EKS (End-to-End)
- After `terraform apply` completes:
  - Infra + Kubernetes manifests are already provisioned.
  - Verify EKS resources:
    ```bash
    aws eks update-kubeconfig --name <cluster-name> --region <region>
    kubectl get nodes
    kubectl get pods
    ```
  - Check logs:
    ```bash
    kubectl logs <pod-name>
    ```
- Send test messages to the live SQS queue to trigger fraud detection:
  ```bash
  aws sqs send-message \
    --queue-url <your-queue-url> \
    --message-body '{"accountId": "123", "amount": 9999}'
  ```
- Observe detection results via CloudWatch logs or alerts.
- For resilience validation, simulate failures (pod eviction, node drain, etc.) — see [`test/test-report.md`](test/test-report.md).

---

## 4 Contact Information

- Licensed under the **MIT License** – see [LICENSE](LICENSE)
- Author: **Dan Chen**
- Contact: 📧 [dan.chen@139.com](mailto:dan.chen@139.com)
