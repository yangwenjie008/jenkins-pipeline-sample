package com.pipeline

/**
 * Common utility methods for Jenkins Pipeline scripts
 * This class provides helper methods that can be used across different pipeline jobs
 */
class CommonUtils {
    
    /**
     * Formats a timestamp into a readable string
     * @param timestamp The timestamp to format (milliseconds since epoch)
     * @param format The date format pattern (default: "yyyy-MM-dd HH:mm:ss")
     * @return Formatted date string
     */
    static String formatTimestamp(Long timestamp, String format = "yyyy-MM-dd HH:mm:ss") {
        if (timestamp == null) {
            return "N/A"
        }
        
        try {
            def date = new Date(timestamp)
            return date.format(format)
        } catch (Exception e) {
            return "Invalid timestamp: ${timestamp}"
        }
    }
    
    /**
     * Generates a unique build ID based on job name, build number and timestamp
     * @param jobName The name of the job
     * @param buildNumber The build number
     * @return Generated build ID
     */
    static String generateBuildId(String jobName, String buildNumber) {
        return "${jobName}-${buildNumber}-${System.currentTimeMillis()}"
    }
    
    /**
     * Checks if a string is empty or null
     * @param str The string to check
     * @return true if string is null or empty, false otherwise
     */
    static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty()
    }
    
    /**
     * Validates an email address format
     * @param email The email address to validate
     * @return true if email format is valid, false otherwise
     */
    static boolean isValidEmail(String email) {
        if (isEmpty(email)) {
            return false
        }
        
        def emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
        return email.matches(emailPattern)
    }
    
    /**
     * Converts byte count to human readable format
     * @param bytes The number of bytes
     * @param si Use SI units (1000) instead of binary units (1024)
     * @return Human readable byte count
     */
    static String humanReadableByteCount(long bytes, boolean si = false) {
        int unit = si ? 1000 : 1024
        if (bytes < unit) {
            return "${bytes} B"
        }
        int exp = (int) (Math.log(bytes) / Math.log(unit))
        String pre = (si ? "kMGTPE" : "KMGTPE")[exp-1] + (si ? "" : "i")
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre)
    }
    
    /**
     * Masks sensitive information in a string
     * @param input The input string
     * @param maskChar The character to use for masking (default: *)
     * @param visibleChars Number of characters to keep visible at the end (default: 4)
     * @return Masked string
     */
    static String maskSensitiveInfo(String input, String maskChar = "*", int visibleChars = 4) {
        if (isEmpty(input)) {
            return input
        }
        
        if (input.length() <= visibleChars) {
            return maskChar * input.length()
        }
        
        def maskedPart = maskChar * (input.length() - visibleChars)
        def visiblePart = input.substring(input.length() - visibleChars)
        return maskedPart + visiblePart
    }
    
    /**
     * Gets the current timestamp in milliseconds
     * @return Current timestamp
     */
    static long getCurrentTimestamp() {
        return System.currentTimeMillis()
    }
    
    /**
     * Calculates duration between two timestamps
     * @param start Start timestamp
     * @param end End timestamp
     * @return Duration in human readable format
     */
    static String calculateDuration(long start, long end) {
        long duration = end - start
        long seconds = duration / 1000
        long minutes = seconds / 60
        long hours = minutes / 60
        
        if (hours > 0) {
            return "${hours}h ${minutes % 60}m ${seconds % 60}s"
        } else if (minutes > 0) {
            return "${minutes}m ${seconds % 60}s"
        } else {
            return "${seconds}s"
        }
    }
    
    /**
     * Converts a map to string representation
     * @param map The map to convert
     * @param separator The separator between key-value pairs (default: ", ")
     * @return String representation of the map
     */
    static String mapToString(Map map, String separator = ", ") {
        if (map == null) {
            return "null"
        }
        
        return map.collect { key, value -> "${key}=${value}" }.join(separator)
    }
    
    /**
     * Merges two maps, with values from the second map overriding the first
     * @param map1 First map
     * @param map2 Second map
     * @return Merged map
     */
    static Map mergeMaps(Map map1, Map map2) {
        if (map1 == null && map2 == null) {
            return [:]
        }
        
        if (map1 == null) {
            return map2.clone()
        }
        
        if (map2 == null) {
            return map1.clone()
        }
        
        def result = map1.clone()
        result.putAll(map2)
        return result
    }
}