
FROM jenkins/jenkins:lts

USER root

# Install required utilities and existing tools
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        maven \
        curl \
        unzip \
        ca-certificates \
        gnupg \
        jq \
        python3 \
        python3-pip && \
    rm -rf /var/lib/apt/lists/*

# Install Docker CLI from the official Docker repository
RUN install -m 0755 -d /etc/apt/keyrings && \
    curl -fsSL https://download.docker.com/linux/debian/gpg \
      -o /etc/apt/keyrings/docker.asc && \
    chmod a+r /etc/apt/keyrings/docker.asc && \
    echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/debian $(. /etc/os-release && echo "$VERSION_CODENAME") stable" \
      > /etc/apt/sources.list.d/docker.list && \
    apt-get update && \
    apt-get install -y docker-ce-cli && \
    rm -rf /var/lib/apt/lists/*

# Install Terraform
RUN install -m 0755 -d /etc/apt/keyrings && \
    curl -fsSL https://apt.releases.hashicorp.com/gpg \
      | gpg --dearmor -o /etc/apt/keyrings/hashicorp-archive-keyring.gpg && \
    chmod 644 /etc/apt/keyrings/hashicorp-archive-keyring.gpg && \
    echo "deb [signed-by=/etc/apt/keyrings/hashicorp-archive-keyring.gpg] https://apt.releases.hashicorp.com $(. /etc/os-release && echo ${VERSION_CODENAME}) main" \
      > /etc/apt/sources.list.d/hashicorp.list && \
    apt-get update && \
    apt-get install -y terraform && \
    rm -rf /var/lib/apt/lists/*

# Install AWS CLI v2
RUN ARCH="$(dpkg --print-architecture)" && \
    case "$ARCH" in \
      amd64) AWS_ARCH=x86_64 ;; \
      arm64) AWS_ARCH=aarch64 ;; \
      *) echo "Unsupported architecture: $ARCH"; exit 1 ;; \
    esac && \
    curl -fsSL "https://awscli.amazonaws.com/awscli-exe-linux-${AWS_ARCH}.zip" \
      -o /tmp/awscliv2.zip && \
    unzip /tmp/awscliv2.zip -d /tmp && \
    /tmp/aws/install && \
    rm -rf /tmp/aws /tmp/awscliv2.zip

# Install kubectl
RUN ARCH="$(dpkg --print-architecture)" && \
    case "$ARCH" in \
      amd64) KARCH=amd64 ;; \
      arm64) KARCH=arm64 ;; \
      *) echo "Unsupported architecture: $ARCH"; exit 1 ;; \
    esac && \
    curl -fsSL https://dl.k8s.io/release/stable.txt \
      -o /tmp/k8s-version && \
    K8S_VERSION="$(cat /tmp/k8s-version)" && \
    curl -fsSL "https://dl.k8s.io/release/${K8S_VERSION}/bin/linux/${KARCH}/kubectl" \
      -o /usr/local/bin/kubectl && \
    chmod +x /usr/local/bin/kubectl && \
    rm -f /tmp/k8s-version

# Verify installations
RUN terraform version && \
    aws --version && \
    kubectl version --client && \
    mvn -version && \
    docker --version

USER jenkins
