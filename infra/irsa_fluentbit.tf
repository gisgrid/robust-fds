resource "aws_iam_policy" "fluentbit_cloudwatch_policy" {
  name        = "fluentbit-cloudwatch-policy"
  description = "Allow Fluent Bit to write logs to CloudWatch"

  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect   = "Allow",
        Action   = [
          "logs:CreateLogStream",
          "logs:PutLogEvents",
          "logs:DescribeLogStreams"
        ],
        Resource = "arn:aws:logs:<aws_region>:<aws_account_id>:log-group:/fraud-detection/app:*"
      }
    ]
  })
}

data "aws_iam_policy_document" "fluentbit_assume_role" {
  statement {
    effect = "Allow"
    actions = ["sts:AssumeRoleWithWebIdentity"]
    principals {
      type        = "Federated"
      identifiers = [module.eks.oidc_provider_arn]
    }

    condition {
      test     = "StringEquals"
      variable = "${replace(module.eks.oidc_provider_url, "https://", "")}:sub"
      values   = ["system:serviceaccount:kube-system:fluent-bit"]
    }
  }
}

resource "aws_iam_role" "fluentbit_role" {
  name               = "eks-fluentbit-role"
  assume_role_policy = data.aws_iam_policy_document.fluentbit_assume_role.json
}

resource "aws_iam_role_policy_attachment" "fluentbit_policy_attach" {
  role       = aws_iam_role.fluentbit_role.name
  policy_arn = aws_iam_policy.fluentbit_cloudwatch_policy.arn
}

output "fluentbit_irsa_role_arn" {
  value = aws_iam_role.fluentbit_role.arn
}
