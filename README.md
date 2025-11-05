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

## 将项目推送到GitHub

要将此项目推送到GitHub，请按照以下步骤操作：

### 1. 在GitHub上创建新仓库

1. 登录到您的GitHub账户
2. 点击右上角的"+"号，选择"New repository"
3. 为仓库输入一个名称（例如：jenkins-pipeline-sample）
4. （可选）添加描述，例如："Jenkins Pipeline Shared Library Sample Project"
5. 选择仓库的可见性（Public或Private）
6. **重要**：不要初始化仓库（不要勾选"Initialize this repository with a README"、".gitignore"或"License"）
7. 点击"Create repository"

创建完成后，GitHub会显示仓库页面，其中包含推送现有仓库的说明。

### 2. 将本地项目推送到GitHub

如果您已经按照前面的步骤初始化了Git仓库，可以使用以下命令：

```bash
# 添加GitHub远程仓库（将URL替换为您在GitHub上创建的仓库URL）
git remote add origin https://github.com/yourusername/jenkins-pipeline-sample.git

# 验证远程仓库已正确添加
git remote -v

# 推送代码到GitHub
git push -u origin master
```

如果您还没有初始化Git仓库，请按以下步骤操作：

```bash
# 初始化Git仓库
git init

# 添加所有文件
git add .

# 设置您的Git用户信息（如果尚未设置）
git config --global user.email "you@example.com"
git config --global user.name "Your Name"

# 创建初始提交
git commit -m "Initial commit"

# 添加GitHub远程仓库（将URL替换为您在GitHub上创建的仓库URL）
git remote add origin https://github.com/yourusername/jenkins-pipeline-sample.git

# 推送代码到GitHub
git push -u origin master
```

### 3. GitHub身份验证

从2021年8月13日起，GitHub不再支持使用用户名和密码进行Git操作。您需要使用个人访问令牌或SSH密钥进行身份验证。

#### 使用个人访问令牌（推荐）

1. 在GitHub上创建个人访问令牌：
   - 登录GitHub并进入"Settings" > "Developer settings" > "Personal access tokens"
   - 点击"Generate new token"
   - 添加描述，选择`repo`权限
   - 生成令牌并复制它

2. 使用令牌进行身份验证：
   ```bash
   # 方法1：更新远程URL包含令牌
   git remote set-url origin https://<your-token>@github.com/yourusername/jenkins-pipeline-sample.git
   
   # 方法2：在推送时输入令牌作为密码
   git push -u origin main
   ```
   当提示输入密码时，输入您的个人访问令牌。

#### 使用SSH密钥（更安全）

1. 生成SSH密钥：
   ```bash
   ssh-keygen -t ed25519 -C "your_email@example.com"
   ```

2. 将SSH密钥添加到ssh-agent：
   ```bash
   eval "$(ssh-agent -s)"
   ssh-add ~/.ssh/id_ed25519
   ```

3. 将公钥添加到GitHub：
   - 复制公钥：`cat ~/.ssh/id_ed25519.pub`
   - 在GitHub中添加SSH密钥：Settings > SSH and GPG keys > New SSH key

4. 更新远程URL使用SSH：
   ```bash
   git remote set-url origin git@github.com:yourusername/jenkins-pipeline-sample.git
   ```

5. 推送代码：
   ```bash
   git push -u origin main
   ```

#### 验证SSH连接

配置SSH密钥后，您可以测试与GitHub的连接：
```bash
ssh -T git@github.com
```

如果看到类似"Hi username! You've successfully authenticated..."的消息，表示SSH配置成功。

### 3. 后续推送更改

在进行更改并提交后，可以使用以下命令推送：

```bash
# 添加更改的文件
git add .

# 提交更改
git commit -m "描述您的更改"

# 推送到GitHub
git push
```

### 4. 处理常见问题

如果在推送时遇到问题，可能需要：

1. **强制推送**（谨慎使用，会覆盖远程历史）：
   ```bash
   git push -f origin master
   ```

2. **拉取远程更改**（在推送前合并远程更改）：
   ```bash
   git pull origin master
   ```

3. **检查远程仓库URL**：
   ```bash
   git remote -v
   ```

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

```
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

## 注意事项

1. 该项目使用Java 17进行编译和测试
2. 由于Jenkins共享库的特殊性质，传统的代码覆盖率工具可能无法提供准确的覆盖率数据
3. 最重要的指标是确保所有共享库函数都有相应的测试用例
4. 测试与源文件的比例应尽可能保持平衡