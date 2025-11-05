// Script object for Jenkins pipeline steps
def script

def sayHello(String name){
    script.echo "Hello ${name}"
}

def runBuild(String app){
    script.sh "echo Building ${app}"
}

def deployToEnv(String env){
    script.sh "echo Deploying to ${env}"
}

return this