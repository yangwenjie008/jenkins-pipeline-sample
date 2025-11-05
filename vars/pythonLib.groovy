/**
 * Global variable for Python functionality
 * This provides a secure way to execute Python scripts in Jenkins Pipelines
 * following Jenkins security best practices
 */

def execute(String scriptContent) {
    return com.pipeline.PythonExecutor.executePythonScript(scriptContent, this)
}

def executeFile(String scriptPath) {
    return com.pipeline.PythonExecutor.executePythonFile(scriptPath, this)
}

def executeInline(String pythonCode) {
    return com.pipeline.PythonExecutor.executeInlinePython(pythonCode, this)
}

def call() {
    // This method is required for global variables
    // It can be empty or contain initialization logic
}