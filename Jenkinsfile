pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Frontend - user-page') {
            steps {
                // node:18-alpine 컨테이너에서 Vue 앱 빌드
                // $WORKSPACE는 Jenkins 환경변수 — 호스트 경로와 1:1 매핑됨
                sh '''
                    docker run --rm \
                      -v "$WORKSPACE/user-page/frontend:/app" \
                      -w /app \
                      node:18-alpine \
                      sh -c "npm ci && npm run build"
                '''
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
                // ① 프론트엔드 빌드 결과물을 Nginx 서빙 경로로 복사
                // Jenkins 컨테이너는 /home/ubuntu에 접근 불가 → Alpine 컨테이너로 호스트 경로 마운트해 복사
                sh '''
                    docker run --rm \
                      -v "$WORKSPACE/user-page/frontend/build:/src" \
                      -v /home/ubuntu/user/frontend:/dst \
                      alpine sh -c "cp -r /src/. /dst/"
                '''

                // ② 프로젝트명 'potal' 고정, 앱 컨테이너만 재시작 (MySQL 제외)
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
