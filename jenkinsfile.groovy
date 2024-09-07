task_branch = "${TEST_BRANCH_NAME}"
def branch_cutted = task_branch.contains("origin") ? task_branch.split('/')[1] : task_branch.trim()
currentBuild.displayName = "$branch_cutted"
base_git_url = "https://github.com/Anton-Bobr/for_M_G.git"

pipeline {
    agent any
    tools {
        gradle 'gradle_jenkins'
    }
    stages {
        stage('Clone Repo') {
            steps {
                getProject("$base_git_url", "$branch_cutted")
                scriptExec("java --version")
                scriptExec("gradle --version")
            }
        }
        stage('Test') {
            steps {
                runTestWithTag("SubscribeToNewUsers")
            }
        }
        stage('Allure') {
            steps {
                generateAllure()
            }
        }
    }
}

def getProject(String repo, String branch) {
    cleanWs()
    checkout scm: [
            $class           : 'GitSCM', branches: [[name: branch]],
            userRemoteConfigs: [[
                                        url: repo
                                ]]
    ]
}

def runTestWithTag(String tag) {
    try {
        labelledShell(label: "Run ${tag}", script: "chmod +x gradlew \n./gradlew -x test ${tag}")
    } finally {
        echo "some failed tests"
    }
}

def generateAllure() {
    allure([
            includeProperties: true,
            jdk              : '21',
            properties       : [],
            reportBuildPolicy: 'ALWAYS',
            results          : [[path: 'build/allure-results']]
    ])
}

void scriptExec(String command) {
    OUTPUT = sh(
            script: command,
            returnStdout: true
    ).trim()
    echo "Command: ${command}"
    echo "Command Out: ${OUTPUT}"
}
