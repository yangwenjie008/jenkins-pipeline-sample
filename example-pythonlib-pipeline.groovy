// Import the shared library
library identifier: 'jenkins-pipeline-sample@main', retriever: modernSCM(gitSource(traits: [gitBranchDiscovery()], credentialsId: 'GitHub-ssh', remote: 'git@github.com:yangwenjie008/jenkins-pipeline-sample.git'))

pipeline {
    agent any
    
    stages {
        stage('Demo Python Shared Library') {
            steps {
                script {
                    // Demo execute simple Python script
                    def result1 = pythonLib.executeInline('print("Hello from Python inline execution!")')
                    echo "Python inline execution output: ${result1}"
                    
                    // Demo execute Python script content
                    def scriptContent = '''
import sys
import datetime

def greet(name):
    return f"Hello {name} from Python!"

# Print Python version
print(f"Python version: {sys.version}")

# Print current time
now = datetime.datetime.now()
print(f"Current time: {now}")

# Use function
print(greet("Jenkins"))
'''
                    
                    def result2 = pythonLib.execute(scriptContent)
                    echo "Python script execution output:\n${result2}"
                    
                    // Demo writing and executing a Python file
                    def scriptPath = "demo_script.py"
                    def fileContent = '''
import os
import math

# Print current working directory
print(f"Current working directory: {os.getcwd()}")

# Math operations
print(f"Value of pi: {math.pi}")
print(f"Square root of 144: {math.sqrt(144)}")

# List files in current directory
files = os.listdir('.')
print("Files in current directory:")
for f in files:
    print(f"  {f}")
'''
                    
                    // Write the Python script to a file
                    writeFile file: scriptPath, text: fileContent
                    
                    // Execute the Python file using our library
                    def result3 = pythonLib.executeFile(scriptPath)
                    echo "Python file execution output:\n${result3}"
                    
                    // Clean up
                    sh "rm -f ${scriptPath}"
                }
            }
        }
        
        stage('Advanced Python Examples') {
            steps {
                script {
                    // Demo data processing with Python
                    def dataProcessingScript = '''
import json

# Sample data
data = [
    {"name": "Alice", "age": 30, "city": "New York"},
    {"name": "Bob", "age": 25, "city": "San Francisco"},
    {"name": "Charlie", "age": 35, "city": "Los Angeles"}
]

# Process data
total_age = sum(person["age"] for person in data)
average_age = total_age / len(data)

print(f"Total people: {len(data)}")
print(f"Average age: {average_age}")

# Find oldest person
oldest = max(data, key=lambda person: person["age"])
print(f"Oldest person: {oldest['name']} ({oldest['age']} years old)")
'''
                    
                    def result4 = pythonLib.execute(dataProcessingScript)
                    echo "Data processing output:\n${result4}"
                    
                    // Demo mathematical computation
                    def mathScript = '''
import math

def fibonacci(n):
    if n <= 1:
        return n
    else:
        return fibonacci(n-1) + fibonacci(n-2)

# Calculate and print first 10 fibonacci numbers
print("First 10 Fibonacci numbers:")
for i in range(10):
    print(f"F({i}) = {fibonacci(i)}")

# Calculate some mathematical constants
print(f"\\nMathematical constants:")
print(f"Pi: {math.pi}")
print(f"Euler's number: {math.e}")
print(f"Golden ratio: {(1 + math.sqrt(5)) / 2}")
'''
                    
                    def result5 = pythonLib.execute(mathScript)
                    echo "Mathematical computation output:\n${result5}"
                }
            }
        }
    }
    
    post {
        always {
            echo 'Pipeline completed'
            // Ensure cleanup
            sh 'rm -f demo_script.py || true'
        }
    }
}