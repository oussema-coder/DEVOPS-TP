terraform {
  required_version = ">= 1.5.0"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }

  # Le nom du bucket est passé à terraform init (-backend-config)
  backend "s3" {
    key    = "achat/terraform.tfstate"
    region = "us-east-1"
  }
}

provider "aws" {
  region = "us-east-1"
}

# AWS Academy : rôle pré-existant
data "aws_iam_role" "lab_role" {
  name = "LabRole"
}

data "aws_vpc" "default" {
  default = true
}

# On exclut us-east-1e (non supportée par EKS)
data "aws_subnets" "eks_subnets" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.default.id]
  }
  filter {
    name   = "availability-zone"
    values = ["us-east-1a", "us-east-1b", "us-east-1c", "us-east-1d", "us-east-1f"]
  }
}

# 1. Cluster EKS
resource "aws_eks_cluster" "achat_cluster" {
  name     = "achat-cluster"
  role_arn = data.aws_iam_role.lab_role.arn

  vpc_config {
    subnet_ids = data.aws_subnets.eks_subnets.ids
  }
}

# 2. Nœuds
resource "aws_eks_node_group" "achat_nodes" {
  cluster_name    = aws_eks_cluster.achat_cluster.name
  node_group_name = "achat-nodes"
  node_role_arn   = data.aws_iam_role.lab_role.arn
  subnet_ids      = data.aws_subnets.eks_subnets.ids

  ami_type       = "AL2023_x86_64_STANDARD"
  instance_types = ["t3.medium"]

  scaling_config {
    desired_size = 2
    max_size     = 3
    min_size     = 1
  }

  depends_on = [aws_eks_cluster.achat_cluster]
}

# 3. Outputs
output "cluster_name" {
  description = "Nom du cluster EKS (utilisé par update-kubeconfig)"
  value       = aws_eks_cluster.achat_cluster.name
}

output "cluster_endpoint" {
  description = "Endpoint de l'API server EKS"
  value       = aws_eks_cluster.achat_cluster.endpoint
}

output "cluster_region" {
  description = "Région AWS"
  value       = "us-east-1"
}