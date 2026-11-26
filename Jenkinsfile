
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
                    mvn -B clean verify -s settings.xml
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
                            mvn -B org.sonarsource.scanner.maven:sonar-maven-plugin:5.5.0.6356:sonar \
                                -Dsonar.projectKey=DEVOPS-TP \
                                -Dsonar.projectName=DEVOPS-TP \
                                -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
                                -Dsonar.token="$SONAR_TOKEN" \
                                -s settings.xml
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
                        mvn -B deploy -s settings.xml
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

                    echo "Docker image built successfully."
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

                        echo "$DOCKER_PASSWORD" | docker login \
                            -u "$DOCKER_USERNAME" \
                            --password-stdin

                        docker push "$DOCKER_IMAGE:$BUILD_NUMBER"
                        docker push "$DOCKER_IMAGE:latest"

                        docker logout
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

                        echo "[2] Checking credentials..."

                        test -n "$AWS_ACCESS_KEY_ID"
                        test -n "$AWS_SECRET_ACCESS_KEY"
                        test -n "$AWS_SESSION_TOKEN"

                        echo "Jenkins credentials loaded successfully."

                        echo "[3] Testing AWS authentication..."

                        aws sts get-caller-identity

                        echo "[4] AWS connection successful!"
                    '''
                }
            }
        }

        // ==========================================
        // 10. TERRAFORM INITIALIZATION
        // ==========================================
        stage('Terraform Init') {
            steps {
                dir("${TERRAFORM_DIR}") {
                    sh '''
                        set -e

                        echo "Initializing Terraform..."

                        terraform init

                        echo "Terraform initialization completed."
                    '''
                }
            }
        }

        // ==========================================
        // 11. TERRAFORM VALIDATION
        // ==========================================
        stage('Terraform Validate') {
            steps {
                dir("${TERRAFORM_DIR}") {
                    sh '''
                        set -e

                        echo "Validating Terraform configuration..."

                        terraform validate

                        echo "Terraform validation successful."
                    '''
                }
            }
        }

        // ==========================================
        // 12. TERRAFORM PLAN
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

                            terraform plan -out=tfplan

                            echo "Terraform plan created successfully."
                        '''
                    }
                }
            }
        }

        // ==========================================
        // 13. TERRAFORM APPLY
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

                            terraform apply -auto-approve tfplan

                            echo "Terraform deployment completed successfully."
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
            Build and Tests       : SUCCESS
            JaCoCo Coverage       : SUCCESS
            SonarQube             : SUCCESS
            Nexus Deployment      : SUCCESS
            Docker Build          : SUCCESS
            Docker Push           : SUCCESS
            AWS Connection        : SUCCESS
            Terraform Deployment  : SUCCESS
            ==========================================
            '''
        }

        failure {
            echo 'Pipeline failed. Check Jenkins Console Output.'
        }

        always {
            echo 'Pipeline execution finished.'
        }
    }
}
