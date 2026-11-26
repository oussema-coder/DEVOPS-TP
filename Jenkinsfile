
pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'hamdiarij/devops-tp'
        AWS_DEFAULT_REGION = 'us-west-2'
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
        // 8. DOCKER PUSH
        // ==========================================
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

                        echo "Logging in to Docker Hub..."

                        trap 'docker logout >/dev/null 2>&1 || true' EXIT

                        echo "$DOCKER_PASSWORD" | docker login \
                            -u "$DOCKER_USERNAME" \
                            --password-stdin

                        echo "Pushing versioned image..."

                        docker push "$DOCKER_IMAGE:$BUILD_NUMBER"

                        echo "Pushing latest image..."

                        docker push "$DOCKER_IMAGE:latest"

                        echo "Docker images pushed successfully!"
                    '''
                }
            }
        }

        // ==========================================
        // 9. TEST AWS CONNECTION
        // ==========================================
        stage('Test AWS Connection') {
            steps {
                echo '========== AWS CONNECTION TEST =========='

                withCredentials([
                    string(
                        credentialsId: 'aws_access_key_id',
                        variable: 'AWS_ACCESS_KEY_ID'
                    ),
                    string(
                        credentialsId: 'aws_secret_access_key',
                        variable: 'AWS_SECRET_ACCESS_KEY'
                    ),
                    string(
                        credentialsId: 'aws_session_token',
                        variable: 'AWS_SESSION_TOKEN'
                    )
                ]) {
                    sh '''
                        set +x
                        set -e

                        echo "[1] Checking AWS CLI..."

                        if ! command -v aws >/dev/null 2>&1; then
                            echo "ERROR: AWS CLI is not installed!"
                            exit 1
                        fi

                        aws --version

                        echo "[2] Checking AWS credentials..."

                        test -n "$AWS_ACCESS_KEY_ID"
                        test -n "$AWS_SECRET_ACCESS_KEY"
                        test -n "$AWS_SESSION_TOKEN"

                        echo "AWS credentials loaded successfully!"

                        echo "[3] Testing AWS authentication..."

                        aws sts get-caller-identity

                        echo "[4] AWS connection successful!"
                    '''
                }
            }
        }

        // ==========================================
        // 10. TEST KUBERNETES CONNECTION
        // ==========================================
        stage('Test Kubernetes Connection') {
            steps {
                echo '========== KUBERNETES CONNECTION TEST =========='

                withCredentials([
                    file(
                        credentialsId: 'kubeid',
                        variable: 'KUBECONFIG'
                    ),
                    string(
                        credentialsId: 'aws_access_key_id',
                        variable: 'AWS_ACCESS_KEY_ID'
                    ),
                    string(
                        credentialsId: 'aws_secret_access_key',
                        variable: 'AWS_SECRET_ACCESS_KEY'
                    ),
                    string(
                        credentialsId: 'aws_session_token',
                        variable: 'AWS_SESSION_TOKEN'
                    )
                ]) {
                    sh '''
                        set +x
                        set -e

                        echo "[1] Checking kubectl..."

                        if ! command -v kubectl >/dev/null 2>&1; then
                            echo "ERROR: kubectl is not installed!"
                            exit 1
                        fi

                        kubectl version --client

                        echo "[2] Checking Kubernetes context..."

                        kubectl config current-context

                        echo "[3] Checking cluster connection..."

                        kubectl cluster-info

                        echo "[4] Checking Kubernetes nodes..."

                        kubectl get nodes -o wide

                        echo "[5] Kubernetes connection successful!"
                    '''
                }
            }
        }

        // ==========================================
        // 11. TERRAFORM INIT
        // ==========================================
        stage('Terraform Init') {
            steps {
                withCredentials([
                    string(
                        credentialsId: 'aws_access_key_id',
                        variable: 'AWS_ACCESS_KEY_ID'
                    ),
                    string(
                        credentialsId: 'aws_secret_access_key',
                        variable: 'AWS_SECRET_ACCESS_KEY'
                    ),
                    string(
                        credentialsId: 'aws_session_token',
                        variable: 'AWS_SESSION_TOKEN'
                    )
                ]) {
                    dir('terraform') {
                        retry(3) {
                            sh '''
                                set +x
                                set -e

                                echo "Initializing Terraform..."

                                terraform init -input=false

                                echo "Terraform initialization successful!"
                            '''
                        }
                    }
                }
            }
        }

        // ==========================================
        // 12. TERRAFORM VALIDATE
        // ==========================================
        stage('Terraform Validate') {
            steps {
                dir("${TERRAFORM_DIR}") {
                    sh '''
                        set -e

                        echo "Validating Terraform configuration..."

                        terraform validate

                        echo "Terraform validation successful!"
                    '''
                }
            }
        }

        // ==========================================
        // 13. TERRAFORM PLAN
        // ==========================================
        stage('Terraform Plan') {
            steps {
                withCredentials([
                    string(
                        credentialsId: 'aws_access_key_id',
                        variable: 'AWS_ACCESS_KEY_ID'
                    ),
                    string(
                        credentialsId: 'aws_secret_access_key',
                        variable: 'AWS_SECRET_ACCESS_KEY'
                    ),
                    string(
                        credentialsId: 'aws_session_token',
                        variable: 'AWS_SESSION_TOKEN'
                    )
                ]) {
                    dir("${TERRAFORM_DIR}") {
                        sh '''
                            set +x
                            set -e

                            echo "Creating Terraform execution plan..."

                            terraform plan \
                                -input=false \
                                -out=tfplan

                            echo "Terraform plan created successfully!"
                        '''
                    }
                }
            }
        }

        // ==========================================
        // 14. TERRAFORM APPLY
        // ==========================================
        stage('Terraform Apply') {
            steps {
                withCredentials([
                    string(
                        credentialsId: 'aws_access_key_id',
                        variable: 'AWS_ACCESS_KEY_ID'
                    ),
                    string(
                        credentialsId: 'aws_secret_access_key',
                        variable: 'AWS_SECRET_ACCESS_KEY'
                    ),
                    string(
                        credentialsId: 'aws_session_token',
                        variable: 'AWS_SESSION_TOKEN'
                    )
                ]) {
                    dir("${TERRAFORM_DIR}") {
                        sh '''
                            set +x
                            set -e

                            echo "Applying Terraform configuration..."

                            terraform apply \
                                -input=false \
                                -auto-approve \
                                tfplan

                            echo "Terraform deployment completed successfully!"
                        '''
                    }
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

            Build and Tests        : SUCCESS
            JaCoCo Coverage        : SUCCESS
            SonarQube              : SUCCESS
            Nexus Deployment       : SUCCESS
            Docker Build           : SUCCESS
            Docker Push            : SUCCESS
            AWS Connection         : SUCCESS
            Kubernetes Connection  : SUCCESS
            Terraform Init         : SUCCESS
            Terraform Validate     : SUCCESS
            Terraform Plan         : SUCCESS
            Terraform Apply        : SUCCESS

            ==========================================
            ALL STAGES COMPLETED SUCCESSFULLY!
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
