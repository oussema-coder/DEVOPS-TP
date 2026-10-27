
pipeline {
    agent {
        docker {
            image 'maven:3.9-eclipse-temurin-21'
        }
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Source code checked out from GitHub'
            }
        }

        stage('Build and Test') {
            steps {
                sh 'mvn -B clean verify'
            }
        }
    }

    post {
        success {
            echo 'Build and tests passed!'
        }

        failure {
            echo 'Build failed. Check Console Output.'
        }
    }
}
