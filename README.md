# Jenkins Pipeline Shared Library Sample

这是一个Jenkins Pipeline共享库示例项目，展示了如何创建、测试和维护Jenkins共享库。

## 项目结构

```
.
├── vars/                    # 共享库步骤定义
│   └── mySteps.groovy      # 示例共享步骤
├── src/test/groovy/        # 测试代码
│   └── MyStepsSpec.groovy  # Spock测试
├── build.gradle            # 构建配置
├── gradle.properties       # Gradle配置
├── Jenkinsfile             # Jenkins Pipeline 示例
├── example-pipeline.groovy # 详细 Pipeline 示例
└── README.md              # 本文件
```

## 先决条件

- Java 17 或更高版本
- Gradle 6.7.1 或更高版本

## 在本地运行测试

在将共享库部署到Jenkins之前，可以在本地运行测试以确保代码质量。

### 构建项目

要构建项目，请运行以下命令：

```bash
./gradlew build
```

或者，仅编译代码：

```bash
./gradlew compileGroovy
```

### 运行测试

#### 运行所有测试

```bash
./gradlew test
```

#### 运行测试并生成详细输出

```bash
./gradlew test --info
```

#### 清理并重新运行测试

```bash
./gradlew clean test
```

### 测试覆盖率

由于Jenkins共享库主要是脚本文件而不是编译的类文件，传统的代码覆盖率工具（如JaCoCo）可能无法正常工作。因此，我们提供了自定义的覆盖率分析任务。

#### 运行覆盖率分析

```bash
./gradlew analyzeCoverage
```

此命令将输出类似以下内容的分析结果：

```
=== Test Coverage Analysis ===
Source files found: 1
Test files found: 1

Source files:
  - mySteps.groovy

Test files:
  - MyStepsSpec.groovy

Test-to-source file ratio: 1.00

Test results:
  Total tests: 19
  Failures: 0
  Errors: 0
  Skipped: 0
  Success rate: 100.0%
```

#### 生成传统覆盖率报告

虽然可能不完全准确，但您仍然可以生成JaCoCo覆盖率报告：

```bash
./gradlew testCoverage
```

这将执行测试并生成JaCoCo覆盖率报告。

## 在本地运行Jenkins

要在本地运行Jenkins并测试此共享库，您可以按照以下步骤操作：

### 1. 使用Homebrew安装和启动Jenkins（macOS推荐方式）

如果您使用的是macOS并安装了Homebrew，可以通过以下命令安装和启动Jenkins：

```bash
# 安装Jenkins
brew install jenkins

# 启动Jenkins服务（后台运行，开机自启）
brew services start jenkins

# 或者，如果您不想作为服务运行，可以使用以下命令：
# /opt/homebrew/opt/jenkins/bin/jenkins --httpListenAddress=127.0.0.1 --httpPort=8080
```

访问 `http://localhost:8080` 并按照设置向导完成初始配置。

### 2. 安装Docker（跨平台推荐方式）

如果您已安装Docker，可以使用官方Jenkins Docker镜像快速启动Jenkins实例：

```bash
# 拉取并运行Jenkins Docker容器
docker run -d \
  --name jenkins \
  -p 8080:8080 \
  -p 50000:50000 \
  -v jenkins_home:/var/jenkins_home \
  -v /var/run/docker.sock:/var/run/docker.sock \
  jenkins/jenkins:lts
```

访问 `http://localhost:8080` 并按照设置向导完成初始配置。

### 3. 使用WAR包运行Jenkins

如果您不想使用Docker，也可以下载Jenkins WAR包并运行：

```bash
# 下载Jenkins WAR包
wget http://mirrors.jenkins.io/war-stable/latest/jenkins.war

# 运行Jenkins
java -jar jenkins.war
```

访问 `http://localhost:8080` 并按照设置向导完成初始配置。

### 4. 配置共享库

在Jenkins中完成初始设置后，需要配置共享库：

1. 进入"Manage Jenkins" > "Configure System"
2. 找到"Global Pipeline Libraries"部分
3. 点击"Add"按钮添加新的共享库
4. 填写以下信息：
   - Name: `jenkins-pipeline-sample`
   - Default version: `master` (或您希望使用的分支/标签)
   - Retrieval method: Modern SCM (根据您的代码仓库选择)
   - 配置您的Git仓库信息，例如：
     - 项目URL: 您的Git仓库地址 (如 `https://github.com/yourname/jenkins-pipeline-sample.git`)
     - Credentials: 如果是私有仓库，选择相应的凭据

注意：在较新版本的Jenkins中，可能没有"Local"选项。在这种情况下，您需要将代码推送到Git仓库，并通过SCM方式配置共享库。

### 5. 创建测试Pipeline Job

1. 在Jenkins主页点击"New Item"
2. 输入项目名称，例如"test-shared-library"
3. 选择"Pipeline"类型并点击"OK"
4. 在Pipeline部分，可以使用以下示例代码：

```groovy
@Library('jenkins-pipeline-sample') _

pipeline {
    agent any
    stages {
        stage('Hello') {
            steps {
                script {
                    mySteps.sayHello("World")
                }
            }
        }
        stage('Build') {
            steps {
                script {
                    mySteps.runBuild("MyApp")
                }
            }
        }
        stage('Deploy') {
            steps {
                script {
                    mySteps.deployToEnv("production")
                }
            }
        }
    }
}
```

5. 点击"Build Now"运行Pipeline

### 6. 使用"Pipeline script from SCM"功能

除了配置全局共享库外，您还可以使用"Pipeline script from SCM"功能直接从Git仓库运行Pipeline，这种方式无需配置全局共享库。这种方法特别适合开发和测试阶段。

操作步骤：

1. 在Jenkins主页点击"New Item"
2. 输入项目名称，例如"test-shared-library-scm"
3. 选择"Pipeline"类型并点击"OK"
4. 在"Pipeline"部分，选择"Pipeline script from SCM"
5. 选择SCM类型（通常是Git）
6. 配置仓库信息：
   - Repository URL: 您的Git仓库地址
   - Credentials: 如果是私有仓库，选择相应的凭据
   - Branches to build: 例如 `*/master` 或 `*/main`
   - Script Path: `Jenkinsfile` (默认值，如果您的Pipeline文件在仓库根目录下)
7. 点击"Save"

这种方法会直接从您的Git仓库中获取Pipeline脚本并执行，其中可以包含对共享库的引用。

示例Pipeline脚本（Jenkinsfile）：

```groovy
// 在SCM方式中，您仍然可以使用共享库
@Library('jenkins-pipeline-sample') _

pipeline {
    agent any
    stages {
        stage('Setup') {
            steps {
                echo 'Starting pipeline execution with shared library'
            }
        }
        
        stage('Execute Shared Library Functions') {
            steps {
                script {
                    // 调用共享库中的方法
                    mySteps.sayHello("Jenkins User")
                    mySteps.runBuild("MyApplication")
                    mySteps.deployToEnv("staging")
                }
            }
        }
        
        stage('Parameterized Execution') {
            steps {
                script {
                    // 使用参数化方式调用
                    def environments = ["dev", "test", "staging"]
                    for (def env : environments) {
                        echo "Processing environment: ${env}"
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
            echo 'Pipeline execution failed'
        }
    }
}
```

使用"Pipeline script from SCM"的优势：

1. **简化配置**：无需在Jenkins中预先配置全局共享库
2. **版本控制**：Pipeline脚本与代码一起进行版本控制
3. **易于维护**：Pipeline的更改可以直接提交到代码仓库
4. **适合开发**：便于在开发阶段快速测试Pipeline和共享库的集成
5. **团队协作**：团队成员可以共同维护Pipeline脚本

### 7. 开发测试的最佳实践

由于在较新的Jenkins版本中可能没有"Local"选项，以下是推荐的开发测试流程：

1. Fork此项目或创建您自己的Git仓库
2. 将代码推送到您的Git仓库
3. 在Jenkins中配置共享库时，使用您的Git仓库URL
4. 在开发过程中，进行更改后提交并推送到Git仓库
5. 运行Pipeline测试您的更改

或者，您可以使用Jenkins的"Pipeline script from SCM"功能直接从本地Git仓库运行Pipeline，而无需配置全局共享库。