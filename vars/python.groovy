/**
 * Global variable for Python functionality
 * This provides a simpler way to execute Python scripts in Jenkins Pipelines
 */

def execute(scriptContent) {
    return com.pipeline.PythonUtils.executePythonScript(scriptContent)
}

def executeFile(scriptPath) {
    return com.pipeline.PythonUtils.executePythonFile(scriptPath)
}

def call() {
    // This method is required for global variables
    // It can be empty or contain initialization logic
}