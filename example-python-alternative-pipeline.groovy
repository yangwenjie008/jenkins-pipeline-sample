// Import the shared library
library identifier: 'jenkins-pipeline-sample@main', retriever: modernSCM(gitSource(traits: [gitBranchDiscovery()], credentialsId: 'GitHub-ssh', remote: 'git@github.com:yangwenjie008/jenkins-pipeline-sample.git'))

pipeline {
    agent any
    
    stages {
        stage('Demo Python with sh') {
            steps {
                script {
                    // Alternative approach 1: Using sh step to execute inline Python code
                    def result1 = sh(
                        script: '''python -c "
import sys
print(\"Hello from Python!\")
print(f\"Python version: {sys.version}\")
"''',
                        returnStdout: true
                    )
                    echo "Inline Python output:\n${result1}"
                    
                    // Alternative approach 2: Create and execute Python file using sh and writeFile
                    def pythonScript = '''
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

                    // Write Python script to file
                    writeFile file: 'temp_script.py', text: pythonScript
                    
                    // Execute Python file
                    def result2 = sh(
                        script: 'python temp_script.py',
                        returnStdout: true
                    )
                    echo "Python file execution output:\n${result2}"
                    
                    // Clean up
                    sh 'rm -f temp_script.py'
                }
            }
        }
        
        stage('Demo Python using our library') {
            steps {
                script {
                    // Using our Python library methods
                    def result3 = python.execute('print("Hello from our Python library!")')
                    echo "Library Python output: ${result3}"
                    
                    // Create a file and execute it with our library
                    def scriptContent = '''
import math
print(f"Value of pi: {math.pi}")
print(f"Square root of 16: {math.sqrt(16)}")
'''
                    writeFile file: 'lib_script.py', text: scriptContent
                    def result4 = python.executeFile('lib_script.py')
                    echo "Library Python file execution output:\n${result4}"
                    sh 'rm -f lib_script.py'
                }
            }
        }
    }
    
    post {
        always {
            echo 'Pipeline completed'
            // Ensure cleanup
            sh 'rm -f temp_script.py lib_script.py || true'
        }
    }
}