// Jenkins Pipeline 示例，演示如何使用共享库中的 mySteps

// 引入共享库：从 GitHub 仓库的 main 分支检索代码，使用 SSH 凭据进行访问
library identifier: 'jenkins-pipeline-sample@main', retriever: modernSCM(gitSource(traits: [gitBranchDiscovery()], credentialsId: 'GitHub-ssh', remote: 'git@github.com:yangwenjie008/jenkins-pipeline-sample.git'))

pipeline {
    agent any
    
    // 引入共享库
    // libraries {
    //     lib('jenkins-pipeline-sample')
    // }
    
    stages {
        stage('test'){
            steps{
                script{
                    sh "./gradlew clean test"
                    sh "./gradlew analyzeCoverage"
                }
            }
        }
        stage('Hello') {
            steps {
                script {
                    // 调用 sayHello 方法
                    mySteps.sayHello("World")
                }
            }
        }
        
        stage('Build') {
            steps {
                script {
                    // 调用 runBuild 方法
                    mySteps.runBuild("MySampleApp")
                }
            }
        }
        
        stage('Deploy') {
            steps {
                script {
                    // 调用 deployToEnv 方法
                    mySteps.deployToEnv("production")
                }
            }
        }
        
        stage('Parameterized Deploy') {
            steps {
                script {
                    // 使用变量调用 deployToEnv 方法
                    def environments = ["dev", "staging", "production"]
                    for (def env : environments) {
                        echo "Deploying to ${env}"
                        mySteps.deployToEnv(env)
                    }
                }
            }
        }
    }
    
    post {
        always {
            echo 'Pipeline execution completed'
        }
        success {
            echo 'Pipeline completed successfully'
        }
        failure {
            echo 'Pipeline failed'
        }
    }
}