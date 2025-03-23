/* ======================= */
/* eks/cluster.tf - EKS Cluster */
/* ======================= */
resource "aws_eks_cluster" "this" {
  name     = var.cluster_name
  role_arn = aws_iam_role.eks_cluster.arn

  vpc_config {
    subnet_ids = var.subnet_ids
  }

  depends_on = [aws_iam_role_policy_attachment.eks_cluster_AmazonEKSClusterPolicy]
}

data "aws_eks_cluster" "oidc_data" {
  name = aws_eks_cluster.this.name
}

resource "aws_iam_openid_connect_provider" "this" {
  client_id_list  = ["sts.amazonaws.com"]
  thumbprint_list = ["9e99a48a9960b14926bb7f3b02e22da0c199e2a5"]
  url             = data.aws_eks_cluster.oidc_data.identity[0].oidc[0].issuer
}





