
pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'hamdiarij/devops-tp'
        AWS_DEFAULT_REGION = 'us-east-1'
        TERRAFORM_DIR = 'terraform'
    }

    stages {

        // ==========================================
        // 1. CHECKOUT
        // ==========================================
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        // ==========================================
        // 2. BUILD AND TEST
        // ==========================================
        stage('Build and Test') {
            steps {
                sh '''
                    set -e
                    echo "Building and testing application..."
                    mvn -B clean verify -s settings.xml
                    echo "Build and tests completed successfully!"
                '''
            }
        }

        // ==========================================
        // 3. JACOCO COVERAGE
        // ==========================================
        stage('JaCoCo Coverage') {
            steps {
                script {
                    if (!fileExists('target/site/jacoco/jacoco.xml')) {
                        error 'JaCoCo XML report not found!'
                    }
                }

                archiveArtifacts(
                    artifacts: 'target/site/jacoco/**',
                    fingerprint: true
                )

                junit(
                    testResults: '**/target/surefire-reports/*.xml',
                    allowEmptyResults: true
                )
            }
        }

        // ==========================================
        // 4. JACOCO REPORT
        // ==========================================
        stage('JaCoCo Report') {
            steps {
                jacoco(
                    execPattern: '**/target/*.exec',
                    classPattern: '**/target/classes',
                    sourcePattern: '**/src/main/java'
                )
            }
        }

        // ==========================================
        // 5. SONARQUBE ANALYSIS
        // ==========================================
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    withCredentials([
                        string(
                            credentialsId: 'sonarqube-token',
                            variable: 'SONAR_TOKEN'
                        )
                    ]) {
                        sh '''
                            set +x
                            set -e

                            echo "Starting SonarQube analysis..."

                            mvn -B org.sonarsource.scanner.maven:sonar-maven-plugin:5.5.0.6356:sonar \
                                -Dsonar.projectKey=DEVOPS-TP \
                                -Dsonar.projectName=DEVOPS-TP \
                                -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
                                -Dsonar.token="$SONAR_TOKEN" \
                                -s settings.xml

                            echo "SonarQube analysis completed!"
                        '''
                    }
                }
            }
        }

        // ==========================================
        // 6. DEPLOY TO NEXUS
        // ==========================================
        stage('Deploy to Nexus') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'nexus-credentials',
                        usernameVariable: 'NEXUS_USERNAME',
                        passwordVariable: 'NEXUS_PASSWORD'
                    )
                ]) {
                    sh '''
                        set +x
                        set -e

                        echo "Deploying artifact to Nexus..."
                        mvn -B deploy -s settings.xml
                        echo "Nexus deployment completed!"
                    '''
                }
            }
        }

        // ==========================================
        // 7. DOCKER BUILD
        // ==========================================
        stage('Docker Build') {
            steps {
                sh '''
                    set -e

                    echo "Checking application JAR..."
                    ls -lh target/*.jar

                    echo "Building Docker image..."

                    docker build \
                        -f ./Dockerfile.jenkins \
                        -t "$DOCKER_IMAGE:$BUILD_NUMBER" .

                    docker tag \
                        "$DOCKER_IMAGE:$BUILD_NUMBER" \
                        "$DOCKER_IMAGE:latest"

                    echo "Docker image built successfully!"
                '''
            }
        }

        // ==========================================
        // 8. DOCKER PUSH (DISABLED)
        // ==========================================

        /*
        stage('Docker Push') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-credentials',
                        usernameVariable: 'DOCKER_USERNAME',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )
                ]) {
                    sh '''
                        set +x
                        set -e

                        trap 'docker logout >/dev/null 2>&1 || true' EXIT

                        echo "$DOCKER_PASSWORD" | docker login \
                            -u "$DOCKER_USERNAME" \
                            --password-stdin

                        docker push "$DOCKER_IMAGE:$BUILD_NUMBER"
                        docker push "$DOCKER_IMAGE:latest"

                        echo "Docker images pushed successfully!"
                    '''
                }
            }
        }
        */

        // ==========================================
        // 9. SETUP TERRAFORM
        // ==========================================
        stage('SETUP TERRAFORM') {
            steps {
                withCredentials([
                    string(credentialsId: 'aws_access_key_id', variable: 'AWS_ACCESS_KEY_ID'),
                    string(credentialsId: 'aws-secret-access-key', variable: 'AWS_SECRET_ACCESS_KEY'),
                    string(credentialsId: 'aws-session-token', variable: 'AWS_SESSION_TOKEN')
                ]) {
                    dir("${TERRAFORM_DIR}") {
                        sh '''
                            set -e

                            echo "Initializing Terraform..."
                            terraform init

                            echo "Validating Terraform..."
                            terraform validate

                            echo "Applying Terraform..."
                            terraform apply -auto-approve
                        '''
                    }
                }
            }
        }

        // ==========================================
        // 10. UPDATE KUBECONFIG
        // ==========================================
        stage('UPDATE KUBECONFIG') {
            steps {
                withCredentials([
                    string(credentialsId: 'aws_access_key_id', variable: 'AWS_ACCESS_KEY_ID'),
                    string(credentialsId: 'aws-secret-access-key', variable: 'AWS_SECRET_ACCESS_KEY'),
                    string(credentialsId: 'aws-session-token', variable: 'AWS_SESSION_TOKEN')
                ]) {
                    sh '''
                        set -e

                        echo "Updating Kubernetes configuration..."

                        aws eks update-kubeconfig \
                            --region "$AWS_DEFAULT_REGION" \
                            --name achat-cluster

                        echo "Kubeconfig updated successfully!"
                    '''
                }
            }
        }

        // ==========================================
        // 11. DEPLOY TO AWS KUBERNETES
        // ==========================================
        stage('DEPLOY TO AWS KUBERNETES') {
            steps {
                withCredentials([
                    string(credentialsId: 'aws_access_key_id', variable: 'AWS_ACCESS_KEY_ID'),
                    string(credentialsId: 'aws-secret-access-key', variable: 'AWS_SECRET_ACCESS_KEY'),
                    string(credentialsId: 'aws-session-token', variable: 'AWS_SESSION_TOKEN')
                ]) {
                    sh '''
                        set -e

                        echo "Deploying application to Kubernetes..."

                        kubectl apply -f k8s/deployment.yaml

                        kubectl rollout restart deployment/achat-app

                        kubectl rollout status deployment/achat-app --timeout=180s

                        echo "Kubernetes Pods:"
                        kubectl get pods

                        echo "Kubernetes Service:"
                        kubectl get svc achat-service
                    '''
                }
            }
        }
    }

    // ==========================================
    // POST ACTIONS
    // ==========================================
    post {

        success {
            echo '''
            ==========================================
            CI/CD PIPELINE COMPLETED SUCCESSFULLY!
            ==========================================

            Build and Tests          : SUCCESS
            JaCoCo Coverage          : SUCCESS
            JaCoCo Report            : SUCCESS
            SonarQube Analysis       : SUCCESS
            Nexus Deployment         : SUCCESS
            Docker Build             : SUCCESS
            Terraform Init           : SUCCESS
            Terraform Validate       : SUCCESS
            Terraform Apply          : SUCCESS
            AWS EKS Configuration    : SUCCESS
            Kubernetes Deployment    : SUCCESS

            Docker Push              : DISABLED

            ==========================================
            ALL ENABLED STAGES COMPLETED!
            ==========================================
            '''
        }

        failure {
            echo '''
            ==========================================
            PIPELINE FAILED!
            ==========================================
            Check Jenkins Console Output for details.
            '''
        }

        always {
            echo 'Pipeline execution finished.'
        }
    }
}
