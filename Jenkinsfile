pipeline {
    agent any
    environment {
        ORG = 'cb-kubecd'
        APP_NAME = 'jx-demo'
        CHARTMUSEUM_CREDS = credentials('jenkins-x-chartmuseum')
        MAVEN_OPTS = '-Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn'
    }
    stages {
        stage('CI Build and push snapshot') {
            when {
                branch 'PR-*'
            }
            environment {
                PREVIEW_VERSION = "0.0.0-SNAPSHOT-$BRANCH_NAME-$BUILD_ID"
                PREVIEW_NAMESPACE = "$APP_NAME-$BRANCH_NAME".toLowerCase()
                HELM_RELEASE = "$PREVIEW_NAMESPACE".toLowerCase()
            }
            steps {
                sh "git config --global credential.helper store"
                sh "jx step validate --min-jx-version 1.1.73"
                sh "jx step git credentials"
                sh 'jx step pre extend'

                sh "mvn versions:set -DnewVersion=$PREVIEW_VERSION"
                sh "mvn install"
                sh 'export VERSION=$PREVIEW_VERSION && skaffold build -f skaffold.yaml'

                sh "jx step validate --min-jx-version 1.2.36"
                sh "jx step post build --image \$JENKINS_X_DOCKER_REGISTRY_SERVICE_HOST:\$JENKINS_X_DOCKER_REGISTRY_SERVICE_PORT/$ORG/$APP_NAME:$PREVIEW_VERSION"


                dir('./charts/preview') {

                    sh "make preview"
                    sh "jx preview --app $APP_NAME --dir ../.."

                }
            }
        }
        stage('Build Release') {
            when {
                branch 'master'
            }
            steps {
                sh 'jx step pre extend'
                git 'https://github.com/cb-kubecd/jx-demo.git'

                sh "git config --global credential.helper store"
                sh "jx step validate --min-jx-version 1.1.73"
                sh "jx step git credentials"
                // so we can retrieve the version in later steps
                sh "echo \$(jx-release-version) > VERSION"
                sh "mvn versions:set -DnewVersion=\$(cat VERSION)"

                dir('./charts/jx-demo') {
                    sh "make tag"
                }

                sh 'mvn clean deploy'

                sh 'export VERSION=`cat VERSION` && skaffold build -f skaffold.yaml'

                sh "jx step post build --image \$JENKINS_X_DOCKER_REGISTRY_SERVICE_HOST:\$JENKINS_X_DOCKER_REGISTRY_SERVICE_PORT/$ORG/$APP_NAME:\$(cat VERSION)"

            }
        }
        stage('Promote to Environments') {
            when {
                branch 'master'
            }
            steps {
                dir('./charts/jx-demo') {
                    sh 'jx step changelog --version v\$(cat ../../VERSION)'
                    // release the helm chart
                    sh 'make release'
                    // promote through all 'Auto' promotion Environments
                    sh 'jx promote -b --all-auto --timeout 1h --version \$(cat ../../VERSION) --no-wait'
                }
            }
        }
    }
    post {
        always {
            sh 'jx step post run'
        }
    }
}
