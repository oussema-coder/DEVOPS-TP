
output "cluster_name" {
  description = "EKS cluster name"
  value       = module.eks.cluster_name
}

output "cluster_endpoint" {
  description = "EKS API endpoint"
  value       = module.eks.cluster_endpoint
}

output "vpc_id" {
  description = "Created VPC ID"
  value       = module.vpc.vpc_id
}

output "worker_security_group_id" {
  description = "EKS worker security group"
  value       = module.eks.node_security_group_id
}
