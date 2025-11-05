package com.pipeline

/**
 * PythonExecutor - A safe and secure way to execute Python scripts in Jenkins Pipelines
 * This class follows Jenkins security best practices and avoids using restricted methods
 */
class PythonExecutor {
    
    /**
     * Executes a Python script using the sh step approach for maximum compatibility
     * This method avoids using restricted Java methods like File.createTempFile
     * 
     * @param scriptContent The Python script content to execute
     * @param steps The workflow steps object (required for executing sh commands)
     * @return The output of the Python script execution
     */
    static String executePythonScript(String scriptContent, steps) {
        // Generate a unique file name using timestamp and random number
        def timestamp = System.currentTimeMillis()
        def random = new Random().nextInt(1000)
        def fileName = "python_script_${timestamp}_${random}.py"
        
        try {
            // Write the script content to a file using Jenkins writeFile step
            steps.writeFile file: fileName, text: scriptContent
            
            // Execute the Python script and capture output
            def result = steps.sh(
                script: "python ${fileName}",
                returnStdout: true
            )
            
            return result
        } finally {
            // Clean up the temporary file
            try {
                steps.sh "rm -f ${fileName}"
            } catch (Exception e) {
                // Ignore cleanup errors
            }
        }
    }
    
    /**
     * Executes a Python script from a file
     * 
     * @param scriptPath The path to the Python script file
     * @param steps The workflow steps object (required for executing sh commands)
     * @return The output of the Python script execution
     */
    static String executePythonFile(String scriptPath, steps) {
        // Check if file exists
        def fileExists = steps.sh(
            script: "test -f ${scriptPath} && echo 'exists' || echo 'missing'",
            returnStdout: true
        ).trim()
        
        if (fileExists == 'missing') {
            throw new RuntimeException("Python script file not found: ${scriptPath}")
        }
        
        // Execute the Python script and capture output
        def result = steps.sh(
            script: "python ${scriptPath}",
            returnStdout: true
        )
        
        return result
    }
    
    /**
     * Executes inline Python code directly without creating a file
     * 
     * @param pythonCode The Python code to execute
     * @param steps The workflow steps object (required for executing sh commands)
     * @return The output of the Python code execution
     */
    static String executeInlinePython(String pythonCode, steps) {
        // Escape quotes for shell command
        def escapedCode = pythonCode.replace('"', '\\"').replace("'", "\\'")
        
        // Execute the Python code and capture output
        def result = steps.sh(
            script: "python -c \"${escapedCode}\"",
            returnStdout: true
        )
        
        return result
    }
}