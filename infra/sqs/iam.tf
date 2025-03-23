/* ======================= */
/* sqs/iam.tf - IAM Role for SQS Access (optional) */
/* ======================= */
resource "aws_iam_policy" "sqs_access" {
  name        = "sqs-access-policy"
  description = "IAM policy to allow access to SQS queues"

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "sqs:SendMessage",
          "sqs:ReceiveMessage",
          "sqs:DeleteMessage",
          "sqs:GetQueueAttributes"
        ]
        Resource = [
          aws_sqs_queue.transaction.arn,
          aws_sqs_queue.fraud_alert.arn
        ]
      }
    ]
  })
}

