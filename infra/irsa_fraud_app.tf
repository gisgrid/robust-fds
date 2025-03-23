resource "aws_iam_policy" "fraud_app_policy" {
  name        = "fraud-app-policy"
  description = "Policy for fraud detection app to access ECR and SQS"

  policy = jsonencode({
	Version = "2012-10-17",
	Statement = [
		{
		Effect = "Allow",
		Action = [
			"ecr:GetAuthorizationToken",
			"ecr:BatchCheckLayerAvailability",
			"ecr:GetDownloadUrlForLayer",
			"ecr:BatchGetImage"
		],
		Resource = "*"
		},
		{
		Effect = "Allow",
		Action = [
			"sqs:SendMessage",
			"sqs:ReceiveMessage",
			"sqs:DeleteMessage",
			"sqs:GetQueueAttributes"
		],
		Resource = [
			"arn:aws:sqs:<aws_region>:<aws_account_id>:transaction-queue",
			"arn:aws:sqs:<aws_region>:<aws_account_id>:fraud-alert-queue"
		]
		},
		{
		Effect = "Allow",
		Action = [
			"cloudwatch:PutMetricData"
		],
		Resource = "*"
		}
	]
	})
}

data "aws_eks_cluster" "cluster" {
  name = module.eks.cluster_name
}

data "aws_iam_policy_document" "fraud_assume_role" {
  statement {
    effect = "Allow"

    principals {
      type        = "Federated"
      identifiers = [module.eks.oidc_provider_arn]  # ✅ Use output from eks module
    }

    actions = ["sts:AssumeRoleWithWebIdentity"]

    condition {
      test     = "StringEquals"
      variable = "${replace(module.eks.oidc_provider_url, "https://", "")}:sub"
      values   = ["system:serviceaccount:fraud-detection:fraud-app-sa"]
    }
  }
}

resource "aws_iam_role" "fraud_app_role" {
  name               = "eks-fraud-app-role"
  assume_role_policy = data.aws_iam_policy_document.fraud_assume_role.json
}

resource "aws_iam_role_policy_attachment" "fraud_app_attach" {
  role       = aws_iam_role.fraud_app_role.name
  policy_arn = aws_iam_policy.fraud_app_policy.arn
}

output "fraud_app_irsa_role_arn" {
  value = aws_iam_role.fraud_app_role.arn
}
