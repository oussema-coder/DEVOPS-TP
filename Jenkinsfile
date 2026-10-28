
pipeline {
    agent any

    stages {
        stage('Build, Test and Deploy to Nexus') {
            steps {
                // Uses settings.xml configured with Nexus credentials
                sh 'mvn -B clean deploy -s settings.xml'
            }
        }}

    post {
        success {
            echo 'Build and tests passed!'
        }
        failure {
            echo 'Build failed. Check Console Output.'
        }
    }
}
