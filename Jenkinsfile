pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                // Pulls code from your GitHub repository
                checkout scm
            }
        }

        stage('SonarQube Analysis') {
            steps {
                // 'SonarQube' must match the name you configured in Jenkins Manage Jenkins > System
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn clean verify sonar:sonar -Dsonar.projectKey=DEVOPS-TP -Dsonar.projectName="DEVOPS-TP" -s settings.xml'
                }
            }
        }

        stage('Build, Test and Deploy to Nexus') {
            steps {
                // Uses settings.xml configured with Nexus credentials
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