@Field def IN_PROGRESS = io.jenkins.plugins.checks.api.ChecksStatus.valueOf("IN_PROGRESS")
@Field def SUCCESS     = io.jenkins.plugins.checks.api.ChecksStatus.valueOf("SUCCESS")
@Field def FAILURE     = io.jenkins.plugins.checks.api.ChecksStatus.valueOf("FAILURE")

pipeline {
    agent {
        node {
            label 'docker-agent-jdk17'
        }
    }

    environment {
        MONGO_HOST_IP   = credentials('MONGO_HOST_IP')
        MINIO_ENDPOINT  = credentials('MINIO_ENDPOINT')
        MINIO_BUCKET    = credentials('MINIO_BUCKET')
        MINIO_USERNAME  = credentials('MINIO_USERNAME')
        MINIO_PASSWORD  = credentials('MINIO_PASSWORD')
    }

    stages {
        stage('Checkout') {
            steps {
                publishChecks name: 'Checkout', status: IN_PROGRESS, summary: 'Cloning repository...'

                git branch: 'master',
                    credentialsId: 'github-app',
                    url: 'https://github.com/changi1122/portfolio-website.git'
            }
            post {
                success {
                    publishChecks name: 'Checkout', status: SUCCESS, summary: 'Checkout completed successfully ✅'
                }
                failure {
                    publishChecks name: 'Checkout', status: FAILURE, summary: 'Checkout failed ❌'
                }
            }
        }

        stage('Start MongoDB') {
            steps {
                publishChecks name: 'MongoDB', status: IN_PROGRESS, summary: 'Starting MongoDB container...'
                sh '''
                    echo "Starting MongoDB container..."
                    docker run -d --rm --network jenkins_jenkins --name mongo-test mongo:4.4.29

                    echo "Waiting for MongoDB to become ready..."
                    for i in {1..30}; do
                        if docker exec mongo-test mongosh --eval "db.adminCommand('ping')" >/dev/null 2>&1; then
                            echo "MongoDB is up!"
                            break
                        fi
                        echo "Waiting... ($i/30)"
                        sleep 2
                    done
                '''
            }
            post {
                success {
                    publishChecks name: 'MongoDB', status: SUCCESS, summary: 'MongoDB is running ✅'
                }
                failure {
                    publishChecks name: 'MongoDB', status: FAILURE, summary: 'MongoDB failed to start ❌'
                }
            }
        }

        stage('Build') {
            steps {
                publishChecks name: 'Build', status: IN_PROGRESS, summary: 'Building project...'
                sh 'chmod +x gradlew'
                sh './gradlew build -x test'
            }
            post {
                success {
                    publishChecks name: 'Build', status: SUCCESS, summary: 'Build succeeded ✅'
                }
                failure {
                    publishChecks name: 'Build', status: FAILURE, summary: 'Build failed ❌'
                }
            }
        }

        stage('Test') {
            steps {
                publishChecks name: 'Test', status: IN_PROGRESS, summary: 'Running tests...'
                sh '''
                    export FILE_DIR="$WORKSPACE/tmp_files/"
                    export IMAGE_DIR="$WORKSPACE/tmp_images/"
                    export VIDEO_DIR="$WORKSPACE/tmp_videos/"
                    SPRING_PROFILES_ACTIVE=test ./gradlew test --no-parallel
                '''
            }
            post {
                always {
                    junit 'build/test-results/test/*.xml'
                }
                success {
                    publishChecks name: 'Test', status: SUCCESS, summary: 'All tests passed ✅'
                }
                failure {
                    publishChecks name: 'Test', status: FAILURE, summary: 'Some tests failed ❌'
                }
            }
        }

        stage('Docker Login') {
            steps {
                publishChecks name: 'Docker Login', status: IN_PROGRESS, summary: 'Logging into Docker Hub...'
                withCredentials([usernamePassword(credentialsId: 'DOCKER_HUB', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                }
            }
            post {
                success {
                    publishChecks name: 'Docker Login', status: SUCCESS, summary: 'Docker login successful ✅'
                }
                failure {
                    publishChecks name: 'Docker Login', status: FAILURE, summary: 'Docker login failed ❌'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                publishChecks name: 'Docker Build', status: IN_PROGRESS, summary: 'Building Docker image...'
                sh '''
                    docker build -t changi1122/portfolio-website:latest .
                    docker push changi1122/portfolio-website:latest
                '''
            }
            post {
                success {
                    publishChecks name: 'Docker Build', status: SUCCESS, summary: 'Image built and pushed ✅'
                }
                failure {
                    publishChecks name: 'Docker Build', status: FAILURE, summary: 'Docker build failed ❌'
                }
            }
        }
    }

    post {
        always {
            echo 'Cleaning up MongoDB container...'
            sh '''
                if docker ps -q -f name=mongo-test >/dev/null; then
                    docker stop mongo-test || true
                fi
            '''
        }
        success {
            publishChecks name: 'CI', status: SUCCESS, summary: 'All stages completed successfully 🎉'
        }
        failure {
            publishChecks name: 'CI', status: FAILURE, summary: 'Pipeline failed ❌'
        }
    }
}