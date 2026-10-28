pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    // Use the fully qualified plugin goal instead of just 'sonar:sonar'
                    sh 'mvn clean verify org.sonarsource.scanner.maven:sonar-maven-plugin:3.10.0.2594:sonar -Dsonar.projectKey=DEVOPS-TP -Dsonar.projectName="DEVOPS-TP" -s settings.xml'
                }
            }
        }

        stage('Build, Test and Deploy to Nexus') {
            steps {
                sh 'mvn -B clean deploy -s settings.xml'
            }
        }
    }

    post {
        success {
            echo 'Build, SonarQube analysis, and deployment passed!'
        }
        failure {
            echo 'Build failed. Check Console Output.'
        }
    }
}