
pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'hamdiarij/devops-tp'
        AWS_DEFAULT_REGION = 'us-west-2'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Test') {
            steps {
                sh 'mvn -B clean verify -s settings.xml'
            }
        }

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

        stage('JaCoCo Report') {
            steps {
                jacoco(
                    execPattern: '**/target/*.exec',
                    classPattern: '**/target/classes',
                    sourcePattern: '**/src/main/java'
                )
            }
        }

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
                            -Dsonar.token=$SONAR_TOKEN \
                            -s settings.xml
                        '''
                    }
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'nexus-credentials',
                        usernameVariable: 'NEXUS_USERNAME',
                        passwordVariable: 'NEXUS_PASSWORD'
                    )
                ]) {
                    sh 'mvn -B deploy -s settings.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh '''
                    set -e

                    echo "Checking application JAR..."
                    ls -lh target/*.jar

                    echo "Building Spring Boot application image..."

                    docker build -f ./Dockerfile.jenkins \
                        -t $DOCKER_IMAGE:$BUILD_NUMBER .

                    docker tag $DOCKER_IMAGE:$BUILD_NUMBER \
                        $DOCKER_IMAGE:latest

                    echo "Docker image built successfully."
                '''
            }
        }

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
                        set -e

                        echo "$DOCKER_PASSWORD" | docker login \
                            -u "$DOCKER_USERNAME" --password-stdin

                        docker push $DOCKER_IMAGE:$BUILD_NUMBER
                        docker push $DOCKER_IMAGE:latest

                        docker logout
                    '''
                }
            }
        }

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

                        echo "[1] Checking AWS CLI..."

                        if ! command -v aws >/dev/null 2>&1; then
                            echo "ERROR: AWS CLI is not installed!"
                            exit 1
                        fi

                        aws --version

                        echo "[2] Checking credentials..."

                        test -n "$AWS_ACCESS_KEY_ID" || {
                            echo "ERROR: Access key is empty!"
                            exit 1
                        }

                        test -n "$AWS_SECRET_ACCESS_KEY" || {
                            echo "ERROR: Secret key is empty!"
                            exit 1
                        }

                        test -n "$AWS_SESSION_TOKEN" || {
                            echo "ERROR: Session token is empty!"
                            exit 1
                        }

                        echo "Jenkins credentials loaded successfully."

                        echo "[3] Testing AWS authentication..."

                        aws sts get-caller-identity

                        echo "[4] AWS connection successful!"
                    '''
                }
            }
        }

stage('Test Kubernetes Connection') {
    steps {
        withCredentials([
            file(credentialsId: 'kubeid', variable: 'KUBECONFIG'),
            string(credentialsId: 'aws_access_key_id',
                   variable: 'AWS_ACCESS_KEY_ID'),
            string(credentialsId: 'aws_secret_access_key',
                   variable: 'AWS_SECRET_ACCESS_KEY'),
            string(credentialsId: 'aws_session_token',
                   variable: 'AWS_SESSION_TOKEN')
        ]) {
            sh '''
                set +x

                export AWS_DEFAULT_REGION=us-east-1

                echo "Testing Kubernetes connection..."

                kubectl --kubeconfig="$KUBECONFIG" get nodes
            '''
        }
    }
}
    }

    post {
        success {
            echo 'CI/CD Pipeline completed successfully!'
            echo 'Build, Tests, JaCoCo, SonarQube, Nexus, Docker, AWS and EKS checks passed!'
        }

        failure {
            echo 'Pipeline failed. Check Console Output.'
        }

        always {
            echo 'Pipeline execution finished.'
        }
    }
}
