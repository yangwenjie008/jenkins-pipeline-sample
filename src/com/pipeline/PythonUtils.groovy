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
        // Use Jenkins's built-in functionality to create a temporary file
        def tempDir = System.getProperty("java.io.tmpdir")
        def timestamp = System.currentTimeMillis()
        def random = new Random().nextInt(1000)
        def fileName = "python_script_${timestamp}_${random}.py"
        def tempScript = new File(tempDir, fileName)
        tempScript.write(scriptContent)
        
        try {
            // Execute the Python script using ProcessBuilder for better control
            def command = ["python", tempScript.absolutePath]
            def process = new ProcessBuilder(command).redirectErrorStream(true).start()
            
            def outputStream = new StringBuffer()
            def reader = new BufferedReader(new InputStreamReader(process.inputStream))
            def line
            while ((line = reader.readLine()) != null) {
                outputStream.append(line).append("\n")
            }
            
            process.waitFor()
            
            if (process.exitValue() != 0) {
                throw new RuntimeException("Python script execution failed with exit code: ${process.exitValue()}")
            }
            
            return outputStream.toString()
        } finally {
            // Clean up the temporary file
            if (tempScript.exists()) {
                tempScript.delete()
            }
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
        
        // Execute the Python script using ProcessBuilder for better control
        def command = ["python", scriptFile.absolutePath]
        def process = new ProcessBuilder(command).redirectErrorStream(true).start()
        
        def outputStream = new StringBuffer()
        def reader = new BufferedReader(new InputStreamReader(process.inputStream))
        def line
        while ((line = reader.readLine()) != null) {
            outputStream.append(line).append("\n")
        }
        
        process.waitFor()
        
        if (process.exitValue() != 0) {
            throw new RuntimeException("Python script execution failed with exit code: ${process.exitValue()}")
        }
        
        return outputStream.toString()
    }
}