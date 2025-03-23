/* ============================ */
/* monitoring/cloudwatch.tf - CloudWatch Log Group */
/* ============================ */
resource "aws_cloudwatch_log_group" "this" {
  name              = "/fraud-detection/app"
  retention_in_days = 14
}
