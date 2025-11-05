package com.pipeline

/**
 * Utility methods for executing Python scripts in Jenkins Pipeline
 * This class provides helper methods to run Python code from Jenkins pipelines
 */
class PythonUtils {
    
    /**
     * Executes a Python script passed as a string
     * @param scriptContent The Python script content to execute
     * @return The output of the Python script
     */
    static String executePythonScript(String scriptContent) {
        // Create a temporary file for the script
        def tempScript = File.createTempFile("python_script", ".py")
        tempScript.write(scriptContent)
        
        try {
            // Execute the Python script
            def process = "python ${tempScript.absolutePath}".execute()
            def outputStream = new StringBuffer()
            def errorStream = new StringBuffer()
            process.waitForProcessOutput(outputStream, errorStream)
            
            if (process.exitValue() != 0) {
                throw new RuntimeException("Python script execution failed: ${errorStream.toString()}")
            }
            
            return outputStream.toString()
        } finally {
            // Clean up the temporary file
            tempScript.delete()
        }
    }
    
    /**
     * Executes a Python script file
     * @param scriptPath The path to the Python script file
     * @return The output of the Python script
     */
    static String executePythonFile(String scriptPath) {
        def scriptFile = new File(scriptPath)
        
        if (!scriptFile.exists()) {
            throw new RuntimeException("Python script file not found: ${scriptPath}")
        }
        
        // Execute the Python script
        def process = "python ${scriptFile.absolutePath}".execute()
        def outputStream = new StringBuffer()
        def errorStream = new StringBuffer()
        process.waitForProcessOutput(outputStream, errorStream)
        
        if (process.exitValue() != 0) {
            throw new RuntimeException("Python script execution failed: ${errorStream.toString()}")
        }
        
        return outputStream.toString()
    }
}