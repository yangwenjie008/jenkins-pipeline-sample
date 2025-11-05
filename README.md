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

## 在本地Jenkins上配置SCM

要在本地Jenkins中配置SCM以使用您的GitHub仓库，请按照以下步骤操作：

### 1. 安装必要的插件

确保安装了以下插件（大多数在Jenkins初始安装时已包含）：
- Git Plugin
- GitHub Plugin
- Pipeline Plugin
- Pipeline: GitHub Groovy Libraries (用于共享库)

检查和安装插件的步骤：
1. 登录到Jenkins (http://localhost:8080)
2. 点击"Manage Jenkins" > "Manage Plugins"
3. 在"Available"选项卡中搜索上述插件
4. 选择需要的插件并点击"Install without restart"

### 2. 配置全局共享库（使用SCM方式）

1. 登录到Jenkins (http://localhost:8080)
2. 点击"Manage Jenkins" > "Configure System"
3. 向下滚动到"Global Pipeline Libraries"部分
4. 点击"Add"按钮添加新的共享库
5. 填写以下信息：
   - Name: `jenkins-pipeline-sample`
   - Default version: `main` (或您使用的分支名称)
   - 选择"Modern SCM"
   - 点击"Git"选项
   - Project Repository: `https://github.com/yangwenjie008/jenkins-pipeline-sample.git`
   - Credentials: 如果是私有仓库，需要添加凭据（见下文）
   - 其他选项保持默认

### 3. 配置凭据（用于私有仓库）

如果您使用的是私有GitHub仓库，需要配置凭据：

1. 在"Manage Jenkins" > "Configure System"页面的"Global Pipeline Libraries"部分
2. 在SCM配置中，点击"Credentils"旁边的"Add"按钮
3. 选择"Jenkins"作为域
4. 选择合适的凭据类型：
   - 对于个人访问令牌：选择"Secret text"，并将令牌作为Secret粘贴
   - 对于SSH密钥：选择"SSH Username with private key"
5. 添加ID和描述（例如：github-pat 或 github-ssh）
6. 点击"Add"

### 4. 创建使用SCM的Pipeline Job

1. 在Jenkins主页点击"New Item"
2. 输入项目名称，例如"test-shared-library-scm"
3. 选择"Pipeline"类型并点击"OK"
4. 在"Pipeline"部分，选择"Pipeline script from SCM"
5. 选择SCM类型为"Git"
6. 配置仓库信息：
   - Repository URL: `https://github.com/yangwenjie008/jenkins-pipeline-sample.git`
   - Credentials: 如果是私有仓库，选择之前配置的凭据
   - Branches to build: `*/main`
   - Script Path: `Jenkinsfile` (默认值)
7. 点击"Save"

### 5. 创建Pipeline脚本

确保您的仓库根目录下有[Jenkinsfile](file:///Users/aaron-pc/Documents/gitroot/jenkins-pipeline-sample/Jenkinsfile)，示例内容如下：

``groovy
// 声明使用共享库
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

### 6. 运行Pipeline

1. 在Jenkins中找到您创建的Pipeline Job
2. 点击"Build Now"运行Pipeline
3. 点击构建号查看控制台输出

### 7. 故障排除

如果遇到问题，请检查以下几点：

1. **网络连接**：确保Jenkins服务器可以访问GitHub
2. **凭据配置**：确保已正确配置GitHub凭据
3. **分支名称**：确保分支名称正确（main vs master）
4. **Jenkins权限**：确保Jenkins进程有权限访问Git仓库
5. **防火墙/代理**：如果在企业网络中，可能需要配置代理

## Available Methods

### CommonUtils Methods

项目中包含一个[CommonUtils](file:///Users/aaron-pc/Documents/gitroot/jenkins-pipeline-sample/src/com/pipeline/CommonUtils.groovy#L7-L168)类，提供常用的工具方法，可在Jenkins Pipeline中使用。

```
// 导入并使用CommonUtils类
def commonUtils = new com.pipeline.CommonUtils()

// 格式化时间戳
def formattedTime = commonUtils.formatTimestamp(System.currentTimeMillis())
echo "Current time: ${formattedTime}"

// 生成构建ID
def buildId = commonUtils.generateBuildId("my-job", "123")
echo "Build ID: ${buildId}"

// 验证邮箱
def isValid = commonUtils.isValidEmail("user@example.com")
echo "Email is valid: ${isValid}"

// 其他方法...
```

完整的使用示例请参考 [example-commonutils-pipeline.groovy](file:///Users/aaron-pc/Documents/gitroot/jenkins-pipeline-sample/example-commonutils-pipeline.groovy) 文件。

### Python Methods

The library also provides Python integration capabilities through two different approaches:

#### Approach 1: Using `python` global variable (Direct approach)
1. `python.execute(scriptContent)` - Execute a Python script passed as a string
2. `python.executeFile(scriptPath)` - Execute a Python script from a file

#### Approach 2: Using `pythonLib` global variable (Recommended - Security focused)
1. `pythonLib.executeInline(pythonCode)` - Execute inline Python code directly
2. `pythonLib.execute(scriptContent)` - Execute a Python script passed as a string
3. `pythonLib.executeFile(scriptPath)` - Execute a Python script from a file

The `pythonLib` approach is recommended as it follows Jenkins security best practices and avoids using restricted Java methods.

Example usage:
``groovy
// Execute inline Python code
def result = pythonLib.executeInline('print("Hello from Python!")')

// Execute a Python script from a string
def result = pythonLib.execute('print("Hello from Python script!")')

// Execute a Python script from a file
def result = pythonLib.executeFile('/path/to/script.py')
```

See [example-pythonlib-pipeline.groovy](example-pythonlib-pipeline.groovy) for a complete example.

### Security Considerations

Due to Jenkins sandbox restrictions, some operations may require approval from administrators:

1. If you encounter "Scripts not permitted to use ..." errors, consider using the alternative approach with `sh` step.
2. See [example-python-alternative-pipeline.groovy](example-python-alternative-pipeline.groovy) for examples using `sh` step to execute Python.

Alternative approach using `sh` step:
```groovy
def result = sh(
    script: 'python -c "print(\"Hello from Python!\")"',
    returnStdout: true
)
```
