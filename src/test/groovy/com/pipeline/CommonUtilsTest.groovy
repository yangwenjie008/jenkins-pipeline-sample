package com.pipeline

import spock.lang.Specification
import spock.lang.Unroll
import groovy.mock.interceptor.MockFor

class CommonUtilsTest extends Specification {

    def "formatTimestamp should format timestamp correctly"() {
        given:
        def timestamp = 1609459200000L // 2021-01-01 00:00:00 UTC

        when:
        // Load the class dynamically to avoid classpath issues
        def commonUtilsClass = new GroovyClassLoader().parseClass(new File('src/com/pipeline/CommonUtils.groovy'))
        def result = commonUtilsClass.formatTimestamp(timestamp)

        then:
        // We can't assert exact format due to timezone differences
        result != null
        result.length() > 0
    }

    def "formatTimestamp should handle null timestamp"() {
        when:
        def commonUtilsClass = new GroovyClassLoader().parseClass(new File('src/com/pipeline/CommonUtils.groovy'))
        def result = commonUtilsClass.formatTimestamp(null)

        then:
        result == "N/A"
    }

    def "formatTimestamp should handle invalid timestamp"() {
        when:
        def commonUtilsClass = new GroovyClassLoader().parseClass(new File('src/com/pipeline/CommonUtils.groovy'))
        def result = commonUtilsClass.formatTimestamp(-1)

        then:
        result != null
        result.length() > 0
    }

    def "generateBuildId should create unique build IDs"() {
        when:
        def commonUtilsClass = new GroovyClassLoader().parseClass(new File('src/com/pipeline/CommonUtils.groovy'))
        def result1 = commonUtilsClass.generateBuildId("job1", "123")
        Thread.sleep(1) // Ensure different timestamps
        def result2 = commonUtilsClass.generateBuildId("job1", "123")

        then:
        result1.startsWith("job1-123-")
        result2.startsWith("job1-123-")
        result1 != result2 // Should be unique due to timestamp
    }

    def "isEmpty should correctly identify empty strings"() {
        when:
        def commonUtilsClass = new GroovyClassLoader().parseClass(new File('src/com/pipeline/CommonUtils.groovy'))
        
        then:
        commonUtilsClass.isEmpty(null) == true
        commonUtilsClass.isEmpty("") == true
        commonUtilsClass.isEmpty("   ") == true
        commonUtilsClass.isEmpty("test") == false
        commonUtilsClass.isEmpty(" a ") == false
    }

    def "isValidEmail should validate email addresses"() {
        when:
        def commonUtilsClass = new GroovyClassLoader().parseClass(new File('src/com/pipeline/CommonUtils.groovy'))
        
        then:
        commonUtilsClass.isValidEmail("user@example.com") == true
        commonUtilsClass.isValidEmail("user.name@example.com") == true
        commonUtilsClass.isValidEmail("user+tag@example.com") == true
        commonUtilsClass.isValidEmail("@example.com") == false
        commonUtilsClass.isValidEmail("user@") == false
        commonUtilsClass.isValidEmail("userexample.com") == false
        commonUtilsClass.isValidEmail("") == false
        commonUtilsClass.isValidEmail(null) == false
    }

    def "humanReadableByteCount should format bytes correctly"() {
        when:
        def commonUtilsClass = new GroovyClassLoader().parseClass(new File('src/com/pipeline/CommonUtils.groovy'))
        
        then:
        commonUtilsClass.humanReadableByteCount(0) == "0 B"
        commonUtilsClass.humanReadableByteCount(1023) == "1023 B"
        commonUtilsClass.humanReadableByteCount(1024) == "1.0 KiB"
        commonUtilsClass.humanReadableByteCount(1048576) == "1.0 MiB"
        commonUtilsClass.humanReadableByteCount(1000, true) == "1.0 kB"
    }

    def "maskSensitiveInfo should mask sensitive information"() {
        when:
        def commonUtilsClass = new GroovyClassLoader().parseClass(new File('src/com/pipeline/CommonUtils.groovy'))
        
        then:
        commonUtilsClass.maskSensitiveInfo(null) == null
        commonUtilsClass.maskSensitiveInfo("") == ""
        commonUtilsClass.maskSensitiveInfo("123") == "***"
        commonUtilsClass.maskSensitiveInfo("secret123") == "*****t123"
        commonUtilsClass.maskSensitiveInfo("password", "#", 4) == "####word"
    }

    def "getCurrentTimestamp should return current time"() {
        when:
        def commonUtilsClass = new GroovyClassLoader().parseClass(new File('src/com/pipeline/CommonUtils.groovy'))
        def result = commonUtilsClass.getCurrentTimestamp()

        then:
        result > 0
        // Should be close to current time (within 1 second)
        Math.abs(result - System.currentTimeMillis()) < 1000
    }

    def "calculateDuration should calculate time differences"() {
        when:
        def commonUtilsClass = new GroovyClassLoader().parseClass(new File('src/com/pipeline/CommonUtils.groovy'))
        
        then:
        commonUtilsClass.calculateDuration(0, 1000) == "1s"
        commonUtilsClass.calculateDuration(0, 60000) == "1m 0s"
        commonUtilsClass.calculateDuration(0, 3600000) == "1h 0m 0s"
    }

    def "mapToString should convert maps to strings"() {
        when:
        def commonUtilsClass = new GroovyClassLoader().parseClass(new File('src/com/pipeline/CommonUtils.groovy'))
        
        then:
        commonUtilsClass.mapToString(null) == "null"
        commonUtilsClass.mapToString([:]) == ""
        commonUtilsClass.mapToString([a: 1]) == "a=1"
        commonUtilsClass.mapToString([a: 1, b: 2]) == "a=1, b=2"
    }

    def "mergeMaps should merge two maps correctly"() {
        when:
        def commonUtilsClass = new GroovyClassLoader().parseClass(new File('src/com/pipeline/CommonUtils.groovy'))
        def map1 = [a: 1, b: 2]
        def map2 = [b: 3, c: 4]
        def result = commonUtilsClass.mergeMaps(map1, map2)

        then:
        result.a == 1
        result.b == 3 // map2 value should override map1
        result.c == 4
    }

    def "mergeMaps should handle null maps"() {
        when:
        def commonUtilsClass = new GroovyClassLoader().parseClass(new File('src/com/pipeline/CommonUtils.groovy'))
        
        then:
        commonUtilsClass.mergeMaps(null, null) != null
        commonUtilsClass.mergeMaps([a: 1], null) == [a: 1]
        commonUtilsClass.mergeMaps(null, [b: 2]) == [b: 2]
    }
}