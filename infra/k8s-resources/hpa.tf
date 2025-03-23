
/* ============================== */
/* k8s-resources/hpa.tf          */
/* ============================== */

resource "kubernetes_horizontal_pod_autoscaler" "java_fds" {
  metadata {
    name      = "${var.app_name}-hpa"
    namespace = var.namespace
  }

  spec {
    scale_target_ref {
      kind = "Deployment"
      name = kubernetes_deployment.java_fds.metadata[0].name
      api_version = "apps/v1"
    }

    min_replicas = var.min_replicas
    max_replicas = var.max_replicas

    target_cpu_utilization_percentage = var.target_cpu_utilization
  }
}
