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
        // Use Jenkins's temporary directory mechanism instead of File.createTempFile
        def tempDir = System.getProperty("java.io.tmpdir")
        def fileName = "python_script_${System.currentTimeMillis()}.py"
        def tempScript = new File(tempDir, fileName)
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