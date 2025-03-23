
/* ============================== */
/* k8s-resources/outputs.tf      */
/* ============================== */

output "deployment_name" {
  value = kubernetes_deployment.java_fds.metadata[0].name
}

output "hpa_name" {
  value = kubernetes_horizontal_pod_autoscaler.java_fds.metadata[0].name
}
