
pipeline {
    agent any

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
                            mvn sonar:sonar \
                            -Dsonar.projectKey=DEVOPS-TP \
                            -Dsonar.projectName=DEVOPS-TP \
                            -Dsonar.token=$SONAR_TOKEN \
                            -s settings.xml
                        '''
                    }
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                sh 'mvn -B deploy -s settings.xml'
            }
        }
    }

    post {
        success {
            echo 'Build, SonarQube analysis and Nexus deployment passed!'
        }

        failure {
            echo 'Pipeline failed. Check Console Output.'
        }
    }
}
