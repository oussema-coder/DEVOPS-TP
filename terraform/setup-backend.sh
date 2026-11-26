#!/bin/bash
set -e

BUCKET="${TF_STATE_BUCKET:?Définis TF_STATE_BUCKET (nom de bucket unique globalement)}"
REGION="us-east-1"

if aws s3api head-bucket --bucket "$BUCKET" 2>/dev/null; then
  echo "Bucket $BUCKET existe déjà."
else
  echo "Création du bucket $BUCKET..."
  # us-east-1 ne prend pas de LocationConstraint
  aws s3api create-bucket --bucket "$BUCKET" --region "$REGION"
  aws s3api put-bucket-versioning \
    --bucket "$BUCKET" \
    --versioning-configuration Status=Enabled
fi