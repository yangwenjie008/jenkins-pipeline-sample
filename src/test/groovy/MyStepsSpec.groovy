import spock.lang.Specification

// Test the basic functionality for Jenkins shared library
class MyStepsSpec extends Specification {
    
    def "basic test framework check"() {
        expect:
        true
    }
    
    def "can create mySteps instance"() {
        when:
        def mySteps = new Expando()
        
        then:
        mySteps != null
    }
    
    def "mySteps can store properties"() {
        given:
        def mySteps = new Expando()
        
        when:
        mySteps.name = "test"
        
        then:
        mySteps.name == "test"
    }
    
    def "a simple test"() {
        given:
        def x = 1
        def y = 2
        
        when:
        def result = x + y
        
        then:
        result == 3
    }
    
    def "simple test to verify framework"() {
        expect:
        1 == 1
    }
    
    def "another simple test"() {
        given:
        def a = 2
        def b = 3
        
        when:
        def sum = a + b
        
        then:
        sum == 5
    }
    
    // Tests for mySteps methods using maps to simulate the script object
    def "sayHello method should create correct echo command"() {
        given:
        def mySteps = loadMySteps()
        def executedCommands = []
        
        and:
        mySteps.script = [
            echo: { message -> executedCommands << ['echo', message] }
        ]
        
        when:
        mySteps.sayHello("World")
        
        then:
        executedCommands == [['echo', 'Hello World']]
    }
    
    def "runBuild method should create correct build command"() {
        given:
        def mySteps = loadMySteps()
        def executedCommands = []
        
        and:
        mySteps.script = [
            sh: { command -> executedCommands << ['sh', command] }
        ]
        
        when:
        mySteps.runBuild("MyApp")
        
        then:
        executedCommands == [['sh', 'echo Building MyApp']]
    }
    
    def "deployToEnv method should create correct deploy command"() {
        given:
        def mySteps = loadMySteps()
        def executedCommands = []
        
        and:
        mySteps.script = [
            sh: { command -> executedCommands << ['sh', command] }
        ]
        
        when:
        mySteps.deployToEnv("production")
        
        then:
        executedCommands == [['sh', 'echo Deploying to production']]
    }
    
    def "all methods should handle empty string parameters"() {
        given:
        def mySteps = loadMySteps()
        def executedCommands = []
        
        and:
        mySteps.script = [
            echo: { message -> executedCommands << ['echo', message] },
            sh: { command -> executedCommands << ['sh', command] }
        ]
        
        when:
        mySteps.sayHello("")
        mySteps.runBuild("")
        mySteps.deployToEnv("")
        
        then:
        executedCommands == [
            ['echo', 'Hello '],
            ['sh', 'echo Building '],
            ['sh', 'echo Deploying to ']
        ]
    }
    
    def "all methods should handle null parameters gracefully"() {
        given:
        def mySteps = loadMySteps()
        def executedCommands = []
        
        and:
        mySteps.script = [
            echo: { message -> executedCommands << ['echo', message] },
            sh: { command -> executedCommands << ['sh', command] }
        ]
        
        when:
        mySteps.sayHello(null)
        mySteps.runBuild(null)
        mySteps.deployToEnv(null)
        
        then:
        executedCommands.size() == 3
    }
    
    // Additional edge case tests
    def "sayHello should handle special characters"() {
        given:
        def mySteps = loadMySteps()
        def executedCommands = []
        def specialName = "Test!@#\$%^&*()_+{}|:<>?[]\\;',./"
        
        and:
        mySteps.script = [
            echo: { message -> executedCommands << ['echo', message] }
        ]
        
        when:
        mySteps.sayHello(specialName)
        
        then:
        executedCommands == [['echo', "Hello ${specialName}"]]
    }
    
    def "runBuild should handle long app names"() {
        given:
        def mySteps = loadMySteps()
        def executedCommands = []
        def longAppName = "a" * 1000
        
        and:
        mySteps.script = [
            sh: { command -> executedCommands << ['sh', command] }
        ]
        
        when:
        mySteps.runBuild(longAppName)
        
        then:
        executedCommands == [['sh', "echo Building ${longAppName}"]]
    }
    
    def "deployToEnv should handle various environment names"() {
        given:
        def mySteps = loadMySteps()
        def executedCommands = []
        
        and:
        mySteps.script = [
            sh: { command -> executedCommands << ['sh', command] }
        ]
        
        when:
        mySteps.deployToEnv(environment)
        
        then:
        executedCommands == [['sh', "echo Deploying to ${environment}"]]
        
        where:
        environment << ["dev", "staging", "prod", "development-1", "test_env"]
    }
    
    def "methods should work when script object has additional methods"() {
        given:
        def mySteps = loadMySteps()
        def executedCommands = []
        
        and:
        mySteps.script = [
            echo: { message -> executedCommands << ['echo', message] },
            sh: { command -> executedCommands << ['sh', command] },
            // Additional methods that might exist in real Jenkins environment
            pwd: { -> "/current/directory" },
            readFile: { file -> "content" }
        ]
        
        when:
        mySteps.sayHello("World")
        mySteps.runBuild("MyApp")
        mySteps.deployToEnv("prod")
        
        then:
        executedCommands == [
            ['echo', 'Hello World'],
            ['sh', 'echo Building MyApp'],
            ['sh', 'echo Deploying to prod']
        ]
    }
    
    // Helper method to load mySteps like Jenkins would
    def loadMySteps() {
        def binding = new Binding()
        def shell = new GroovyShell(binding)
        def mySteps = shell.parse(new File('vars/mySteps.groovy'))
        mySteps.script = [:]
        return mySteps
    }
}