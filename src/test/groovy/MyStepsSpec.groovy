import spock.lang.Specification
import spock.lang.Unroll

// Test the basic functionality for Jenkins shared library
class MyStepsSpec extends Specification {
    
    def steps

    def setup() {
        steps = new Expando()
        
        // Mock Jenkins steps
        steps.echo = { String message -> 
            println message
            return message
        }
        
        steps.sh = { String script -> 
            println script
            return "MOCKED: ${script}"
        }
        
        // Add the binding to simulate Jenkins environment
        def binding = new Binding()
        binding.setVariable("echo", steps.echo)
        binding.setVariable("sh", steps.sh)
        
        // Create a custom class loader to inject our binding
        def gcl = new GroovyClassLoader()
        def myStepsClass = gcl.parseClass(new File('vars/mySteps.groovy'))
        def mySteps = myStepsClass.newInstance()
        
        // Set the binding for the script
        mySteps.binding = binding
        
        steps.mySteps = mySteps
    }

    def "sayHello method should create correct echo command"() {
        given:
        def output = new ByteArrayOutputStream()
        System.out = new PrintStream(output)
        
        when:
        steps.mySteps.sayHello("World")
        
        then:
        output.toString().trim() == "Hello World"
    }

    def "runBuild method should create correct build command"() {
        given:
        def output = new ByteArrayOutputStream()
        System.out = new PrintStream(output)
        
        when:
        steps.mySteps.runBuild("MyApp")
        
        then:
        output.toString().trim() == "echo Building MyApp"
    }

    def "deployToEnv method should create correct deploy command"() {
        given:
        def output = new ByteArrayOutputStream()
        System.out = new PrintStream(output)
        
        when:
        steps.mySteps.deployToEnv("production")
        
        then:
        output.toString().trim() == "echo Deploying to production"
    }

    @Unroll
    def "sayHello method should handle special characters: #name"() {
        given:
        def output = new ByteArrayOutputStream()
        System.out = new PrintStream(output)
        
        when:
        steps.mySteps.sayHello(name)
        
        then:
        output.toString().trim() == expected
        
        where:
        name           || expected
        "John Doe"     || "Hello John Doe"
        "Jane Smith"   || "Hello Jane Smith"
        "用户"          || "Hello 用户"
        "Test123"      || "Hello Test123"
        ""             || "Hello"
    }

    @Unroll
    def "runBuild method should handle different app names: #app"() {
        given:
        def output = new ByteArrayOutputStream()
        System.out = new PrintStream(output)
        
        when:
        steps.mySteps.runBuild(app)
        
        then:
        output.toString().trim() == expected
        
        where:
        app           || expected
        "Frontend"    || "echo Building Frontend"
        "Backend"     || "echo Building Backend"
        "Mobile App"  || "echo Building Mobile App"
        "API服务"      || "echo Building API服务"
        "App123"      || "echo Building App123"
        ""            || "echo Building"
    }

    @Unroll
    def "deployToEnv method should handle different environments: #env"() {
        given:
        def output = new ByteArrayOutputStream()
        System.out = new PrintStream(output)
        
        when:
        steps.mySteps.deployToEnv(env)
        
        then:
        output.toString().trim() == expected
        
        where:
        env              || expected
        "dev"            || "echo Deploying to dev"
        "test"           || "echo Deploying to test"
        "staging"        || "echo Deploying to staging"
        "prod"           || "echo Deploying to prod"
        "production"     || "echo Deploying to production"
        "development"    || "echo Deploying to development"
        ""               || "echo Deploying to"
    }

    def "methods should handle null values gracefully"() {
        given:
        def output = new ByteArrayOutputStream()
        System.out = new PrintStream(output)
        
        when:
        steps.mySteps.sayHello(null)
        steps.mySteps.runBuild(null)
        steps.mySteps.deployToEnv(null)
        
        then:
        noExceptionThrown()
    }

    def "methods should handle empty string values"() {
        given:
        def output = new ByteArrayOutputStream()
        System.out = new PrintStream(output)
        
        when:
        steps.mySteps.sayHello("")
        steps.mySteps.runBuild("")
        steps.mySteps.deployToEnv("")
        
        then:
        output.toString().contains("Hello") 
        output.toString().contains("echo Building") 
        output.toString().contains("echo Deploying to")
    }
}