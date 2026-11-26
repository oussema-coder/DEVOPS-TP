
pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'hamdiarij/devops-tp'
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
                    docker build -f dockerfile.jenkins \
                        -t $DOCKER_IMAGE:$BUILD_NUMBER .
                    docker tag $DOCKER_IMAGE:$BUILD_NUMBER $DOCKER_IMAGE:latest
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
        withCredentials([
            string(
                credentialsId: 'aws-access-key-id',
                variable: 'AWS_ACCESS_KEY_ID'
            ),
            string(
                credentialsId: 'aws-secret-access-key',
                variable: 'AWS_SECRET_ACCESS_KEY'
            ),
            string(
                credentialsId: 'aws-session-token',
                variable: 'AWS_SESSION_TOKEN'
            )
        ]) {
            sh '''
                set +x

                export AWS_DEFAULT_REGION=us-west-2

                echo "Checking AWS CLI..."
                aws --version

                echo "Testing AWS authentication..."
                aws sts get-caller-identity
            '''
        }
    }
}

    }

    post {
        success {
            echo 'CI/CD Pipeline completed successfully!'
            echo 'Build, Tests, JaCoCo, SonarQube, Nexus and Docker passed!'
        }

        failure {
            echo 'Pipeline failed. Check Console Output.'
        }

        always {
            echo 'Pipeline execution finished.'
        }
    }
}
