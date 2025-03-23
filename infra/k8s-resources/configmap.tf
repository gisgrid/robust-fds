
/* ============================== */
/* k8s-resources/configmap.tf    */
/* ============================== */

resource "kubernetes_config_map" "fds_config" {
  metadata {
    name      = "${var.app_name}-config"
    namespace = var.namespace
  }

  data = {
    TRANSACTION_QUEUE_URL = var.transaction_queue_url
    FRAUD_ALERT_QUEUE_URL = var.fraud_alert_queue_url
    THRESHOLD_AMOUNT = var.threshold_amount
  }
}
