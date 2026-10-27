
pipeline {
    agent any

    stages {
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
