/**
 * Global variable for Python functionality
 * This provides a simpler way to execute Python scripts in Jenkins Pipelines
 */

def execute(scriptContent) {
    try {
        return com.pipeline.PythonUtils.executePythonScript(scriptContent)
    } catch (Exception e) {
        echo "Error executing Python script: ${e.message}"
        throw e
    }
}

def executeFile(scriptPath) {
    try {
        return com.pipeline.PythonUtils.executePythonFile(scriptPath)
    } catch (Exception e) {
        echo "Error executing Python file '${scriptPath}': ${e.message}"
        throw e
    }
}

def call() {
    // This method is required for global variables
    // It can be empty or contain initialization logic
}