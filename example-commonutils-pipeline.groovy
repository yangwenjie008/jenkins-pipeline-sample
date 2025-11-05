/**
 * Example Jenkins Pipeline demonstrating the usage of CommonUtils class
 * This pipeline shows how to use various methods from the CommonUtils class
 */

// Import the shared library
@Library('jenkins-pipeline-sample') _

pipeline {
    agent any
    
    stages {
        stage('Demo CommonUtils') {
            steps {
                script {
                    // Import the CommonUtils class
                    def commonUtils = new com.pipeline.CommonUtils()
                    
                    // Demo formatTimestamp method
                    def currentTime = System.currentTimeMillis()
                    def formattedTime = commonUtils.formatTimestamp(currentTime)
                    echo "Current time: ${formattedTime}"
                    
                    // Demo generateBuildId method
                    def buildId = commonUtils.generateBuildId("my-app", "123")
                    echo "Generated build ID: ${buildId}"
                    
                    // Demo isEmpty method
                    def emptyCheck1 = commonUtils.isEmpty("")
                    def emptyCheck2 = commonUtils.isEmpty("test")
                    def emptyCheck3 = commonUtils.isEmpty(null)
                    echo "Is empty string empty? ${emptyCheck1}"
                    echo "Is 'test' empty? ${emptyCheck2}"
                    echo "Is null empty? ${emptyCheck3}"
                    
                    // Demo isValidEmail method
                    def validEmail = commonUtils.isValidEmail("user@example.com")
                    def invalidEmail = commonUtils.isValidEmail("invalid.email")
                    def anotherInvalidEmail = commonUtils.isValidEmail("user@")
                    echo "Is 'user@example.com' valid? ${validEmail}"
                    echo "Is 'invalid.email' valid? ${invalidEmail}"
                    echo "Is 'user@' valid? ${anotherInvalidEmail}"
                    
                    // Demo humanReadableByteCount method
                    def fileSize = commonUtils.humanReadableByteCount(1048576)
                    def smallSize = commonUtils.humanReadableByteCount(512)
                    def largeSize = commonUtils.humanReadableByteCount(1073741824)
                    echo "1MB in human readable format: ${fileSize}"
                    echo "512 bytes in human readable format: ${smallSize}"
                    echo "1GB in human readable format: ${largeSize}"
                    
                    // Demo maskSensitiveInfo method
                    def maskedPassword = commonUtils.maskSensitiveInfo("mysecretpassword")
                    def maskedApiKey = commonUtils.maskSensitiveInfo("api123456")
                    echo "Masked password: ${maskedPassword}"
                    echo "Masked API key: ${maskedApiKey}"
                    
                    // Demo getCurrentTimestamp method
                    def timestamp = commonUtils.getCurrentTimestamp()
                    echo "Current timestamp: ${timestamp}"
                    
                    // Demo calculateDuration method
                    def startTime = System.currentTimeMillis() - 5000 // 5 seconds ago
                    def endTime = System.currentTimeMillis()
                    def duration = commonUtils.calculateDuration(startTime, endTime)
                    echo "Duration: ${duration}"
                    
                    // Demo mapToString method
                    def config = [host: "localhost", port: 8080, ssl: true]
                    def configString = commonUtils.mapToString(config)
                    echo "Config: ${configString}"
                    
                    // Demo mergeMaps method
                    def defaultConfig = [host: "localhost", port: 8080, timeout: 30]
                    def overrideConfig = [port: 9090, ssl: true]
                    def mergedConfig = commonUtils.mergeMaps(defaultConfig, overrideConfig)
                    echo "Merged config: ${mergedConfig}"
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