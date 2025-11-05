/**
 * Global variable for CommonUtils functionality
 * This provides a simpler way to access CommonUtils methods in Jenkins Pipelines
 */

def formatTimestamp(Long timestamp, String format = "yyyy-MM-dd HH:mm:ss") {
    return com.pipeline.CommonUtils.formatTimestamp(timestamp, format)
}

def generateBuildId(String jobName, String buildNumber) {
    return com.pipeline.CommonUtils.generateBuildId(jobName, buildNumber)
}

def isEmpty(String str) {
    return com.pipeline.CommonUtils.isEmpty(str)
}

def isValidEmail(String email) {
    return com.pipeline.CommonUtils.isValidEmail(email)
}

def humanReadableByteCount(long bytes, boolean si = false) {
    return com.pipeline.CommonUtils.humanReadableByteCount(bytes, si)
}

def maskSensitiveInfo(String input, String maskChar = "*", int visibleChars = 4) {
    return com.pipeline.CommonUtils.maskSensitiveInfo(input, maskChar, visibleChars)
}

def getCurrentTimestamp() {
    return com.pipeline.CommonUtils.getCurrentTimestamp()
}

def calculateDuration(long start, long end) {
    return com.pipeline.CommonUtils.calculateDuration(start, end)
}

def mapToString(Map map, String separator = ", ") {
    return com.pipeline.CommonUtils.mapToString(map, separator)
}

def mergeMaps(Map map1, Map map2) {
    return com.pipeline.CommonUtils.mergeMaps(map1, map2)
}

def call() {
    // This method is required for global variables
    // It can be empty or contain initialization logic
}

// Fallback method to get the actual CommonUtils class instance if needed
def getInstance() {
    return new com.pipeline.CommonUtils()
}