// Import the shared library
library identifier: 'jenkins-pipeline-sample@main', retriever: modernSCM(gitSource(traits: [gitBranchDiscovery()], credentialsId: 'GitHub-ssh', remote: 'git@github.com:yangwenjie008/jenkins-pipeline-sample.git'))

pipeline {
    agent any
    
    stages {
        stage('Demo Python') {
            steps {
                script {
                    // Demo execute simple Python script with sandbox and timeout
                    def result1 = python.execute(
                        script: 'print("Hello from Python!")',
                        sandbox: true,
                        timeout: 30
                    )
                    echo "Python output: ${result1}"
                    
                    // Demo more complex Python script
                    def scriptContent = '''
import sys
import math

def fibonacci(n):
    if n <= 1:
        return n
    else:
        return fibonacci(n-1) + fibonacci(n-2)

# Calculate fibonacci sequence
n = 10
print(f"First {n} numbers in Fibonacci sequence:")
for i in range(n):
    print(fibonacci(i), end=' ')
print()

# Print Python version
print(f"Python version: {sys.version}")
'''
                    
                    try {
                        // Execute complex script with sandbox and timeout protection
                        def result2 = python.execute(
                            script: scriptContent,
                            sandbox: true,
                            timeout: 60
                        )
                        echo "Complex Python script output:\n${result2}"
                    } catch (Exception e) {
                        echo "Error executing complex Python script: ${e.getMessage()}"
                        throw e
                    }
                    
                    // Demo writing and executing a Python file
                    def tempDir = pwd(tmp: true)
                    def scriptPath = "${tempDir}/test.py"
                    
                    def fileContent = '''
import os
import datetime

# Print current working directory
print(f"Current working directory: {os.getcwd()}")

# Print current time
now = datetime.datetime.now()
print(f"Current time: {now}")

# List files in current directory
files = os.listdir('.')
print("Files in current directory:")
for f in files:
    print(f"  {f}")
'''
                    
                    // Write the Python script to a file
                    writeFile file: scriptPath, text: fileContent
                    
                    // Execute the Python file with sandbox and timeout
                    def result3 = python.executeFile(
                        path: scriptPath,
                        sandbox: true,
                        timeout: 45
                    )
                    echo "Python file execution output:\n${result3}"
                }
            }
        }
    }
    
    post {
        always {
            echo 'Pipeline completed'
        }
    }
}