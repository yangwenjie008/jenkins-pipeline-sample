// Script object for Jenkins pipeline steps


def sayHello(String name){
    echo "Hello ${name}"
}

def runBuild(String app){
    sh "echo Building ${app}"
}

def deployToEnv(String env){
    sh "echo Deploying to ${env}"
}

return this