#!/bin/bash
set -e

BUCKET="${TF_STATE_BUCKET:?Définis TF_STATE_BUCKET}"
REGION="us-east-1"
CLUSTER="achat-cluster"
NODEGROUP="achat-nodes"

cd "$(dirname "$0")"

echo "Initializing Terraform (backend S3)..."
terraform init -input=false -backend-config="bucket=${BUCKET}"

echo "Validating Terraform..."
terraform validate

# Si la ressource existe dans AWS mais pas dans l'état, on l'importe
if aws eks describe-cluster --name "$CLUSTER" --region "$REGION" >/dev/null 2>&1; then
  if ! terraform state list | grep -q '^aws_eks_cluster.achat_cluster$'; then
    echo "Import du cluster existant..."
    terraform import -input=false aws_eks_cluster.achat_cluster "$CLUSTER"
  fi
fi

if aws eks describe-nodegroup --cluster-name "$CLUSTER" --nodegroup-name "$NODEGROUP" \
     --region "$REGION" >/dev/null 2>&1; then
  if ! terraform state list | grep -q '^aws_eks_node_group.achat_nodes$'; then
    echo "Import du node group existant..."
    terraform import -input=false aws_eks_node_group.achat_nodes "${CLUSTER}:${NODEGROUP}"
  fi
fi

echo "Applying Terraform..."
terraform apply -auto-approve -input=false