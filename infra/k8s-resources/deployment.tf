
/* ============================== */
/* k8s-resources/deployment.tf   */
/* ============================== */

resource "kubernetes_deployment" "java_fds" {
  metadata {
    name      = var.app_name
    namespace = var.namespace
    labels = {
      app = var.app_name
    }
  }

  spec {
    replicas = var.replicas
	selector {
      match_labels = {
        app = var.app_name
      }
    }

    template {
      metadata {
        labels = {
          app = var.app_name
        }
      }

      spec {
        service_account_name = "fraud-app-sa" 
		container {
          image = var.image_url
          name  = var.app_name

          resources {
            requests = {
              cpu    = "100m"
              memory = "128Mi"
            }
            limits = {
              cpu    = "500m"
              memory = "512Mi"
            }
          }

          env_from {
            config_map_ref {
              name = kubernetes_config_map.fds_config.metadata[0].name
            }
          }
        }
      }
    }
  }
}
