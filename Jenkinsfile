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
                    sh 'mvn clean verify org.sonarsource.scanner.maven:sonar-maven-plugin:3.10.0.2594:sonar -Dsonar.projectKey=DEVOPS-TP -Dsonar.projectName="DEVOPS-TP" -Dsonar.token=sqa_60ee6ca622b73f11bdd1ec0788cbb4a6feef45a1 -s settings.xml'
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