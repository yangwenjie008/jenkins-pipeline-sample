/**
 * Jenkins Pipeline Shared Library Sample Steps
 * 
 * This file contains sample steps that can be used in Jenkins Pipeline scripts.
 */

def sayHello(String name) {
    // Check if we're running in Jenkins context
    if (binding.hasVariable('echo')) {
        echo "Hello ${name ?: ''}"
    } else {
        // For testing purposes, just print to stdout
        println "Hello ${name ?: ''}"
    }
}

def runBuild(String app) {
    def command = "echo Building ${app ?: ''}"
    // Check if we're running in Jenkins context
    if (binding.hasVariable('sh')) {
        sh command
    } else {
        // For testing purposes, just print to stdout
        println command
    }
}

def deployToEnv(String env) {
    def command = "echo Deploying to ${env ?: ''}"
    // Check if we're running in Jenkins context
    if (binding.hasVariable('sh')) {
        sh command
    } else {
        // For testing purposes, just print to stdout
        println command
    }
}

// Make methods available
return this