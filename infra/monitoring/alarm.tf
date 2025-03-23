/* ============================ */
/* monitoring/alarm.tf - CloudWatch Alarm Example */
/* ============================ */
resource "aws_cloudwatch_metric_alarm" "high_cpu_alarm" {
  alarm_name          = "HighCPUUtilization"
  comparison_operator = "GreaterThanThreshold"
  evaluation_periods  = 2
  metric_name         = "CPUUtilization"
  namespace           = "AWS/EKS"
  period              = 60
  statistic           = "Average"
  threshold           = 75
  alarm_description   = "This alarm triggers when CPU utilization exceeds 75% for 2 minutes."
  dimensions = {
    ClusterName = var.cluster_name
  }
  alarm_actions = [] # You can add SNS topic ARN here if needed
}
