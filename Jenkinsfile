pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build JAR - user-page') {
            steps {
                dir('user-page/backend') {
                    sh 'chmod +x gradlew'
                    sh './gradlew clean build -x test'
                }
            }
        }

        stage('Build JAR - admin-page') {
            steps {
                dir('admin-page/backend') {
                    sh 'chmod +x gradlew'
                    sh './gradlew clean build -x test'
                }
            }
        }

        stage('Docker Build') {
            steps {
                dir('user-page/backend') {
                    sh 'docker build -t user-page:latest .'
                }
                dir('admin-page/backend') {
                    sh 'docker build -t admin-page:latest .'
                }
            }
        }

        stage('Deploy') {
            steps {
                // 프로젝트명 'potal' 고정, 앱 컨테이너만 재시작 (MySQL 제외)
                sh '''
                    docker-compose -p potal \
                        --env-file /var/jenkins_home/.env.potal \
                        up -d --no-deps user-page admin-page
                '''
            }
        }
    }

    post {
        success {
            echo '✅ 배포 성공'
        }
        failure {
            echo '❌ 빌드/배포 실패'
        }
    }
}
