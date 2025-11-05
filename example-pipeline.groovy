// 更详细的 Jenkins Pipeline 示例
// 展示多种使用 mySteps.groovy 的方式

// 方式1: 在 pipeline 块外部声明共享库
@Library('jenkins-pipeline-sample') _

pipeline {
    agent any
    
    stages {
        stage('Setup') {
            steps {
                echo 'Starting pipeline execution'
            }
        }
        
        stage('Greeting') {
            steps {
                script {
                    // 基本用法
                    mySteps.sayHello("Jenkins")
                    
                    // 使用变量
                    def userName = sh(script: 'whoami', returnStdout: true).trim()
                    mySteps.sayHello(userName)
                }
            }
        }
        
        stage('Build Application') {
            steps {
                script {
                    // 构建不同应用
                    def apps = ["Frontend", "Backend", "Mobile"]
                    apps.each { app ->
                        mySteps.runBuild(app)
                    }
                }
            }
        }
        
        stage('Deploy to Environments') {
            steps {
                script {
                    // 部署到不同环境
                    def deploymentMap = [
                        dev: ["Frontend", "Backend"],
                        staging: ["Frontend", "Backend"],
                        production: ["Frontend", "Backend"]
                    ]
                    
                    deploymentMap.each { env, apps ->
                        echo "Deploying to ${env} environment:"
                        apps.each { app ->
                            mySteps.deployToEnv(env)
                        }
                    }
                }
            }
        }
        
        stage('Conditional Deployment') {
            steps {
                script {
                    // 根据参数条件部署
                    def targetEnv = params.TARGET_ENV ?: "dev"
                    def shouldDeploy = params.DEPLOY ?: false
                    
                    if (shouldDeploy) {
                        mySteps.sayHello("Deploying to ${targetEnv}")
                        mySteps.runBuild("MyApp")
                        mySteps.deployToEnv(targetEnv)
                    } else {
                        echo "Deployment skipped"
                    }
                }
            }
        }
    }
    
    post {
        always {
            echo 'Pipeline completed'
        }
        success {
            echo 'All steps completed successfully!'
        }
        failure {
            echo 'Pipeline failed. Please check the logs.'
        }
    }
}

// 方式2: 在单独的脚本中使用共享库
// 这可以在 Jenkins 的 Script Console 或其他 Groovy 脚本中运行
/*
@Library('jenkins-pipeline-sample') _
 
// 直接调用方法
mySteps.sayHello("from script")
mySteps.runBuild("MyApp")
mySteps.deployToEnv("test")
*/