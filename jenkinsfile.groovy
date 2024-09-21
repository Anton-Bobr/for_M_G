def task_branch = "${TEST_BRANCH_NAME}"
def test_case_tag = "${TEST_CASE}"

def branch_cutted = task_branch.contains("origin") ? task_branch.split('/')[1] : task_branch.trim()
base_git_url = "https://github.com/Anton-Bobr/for_M_G.git"

pipeline {
    agent any
    tools {
        gradle 'jenkins_gradle_8.8'
    }
    stages {
        stage('Clone Repo') {
            steps {
                getProject("$base_git_url", "$branch_cutted")
            }
        }
        stage('Build') {
            steps {
                scriptExec("java --version")
                scriptExec("gradle --version")
                scriptExec("gradle wrapper --info")
            }
        }
        stage('Test') {
            steps {
                runTestWithTag(test_case_tag)
            }
        }
    }
    post {
        always {
            generateAllure()
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
        scriptExec("export DISPLAY=:99")
        echo $DISPLAY
        labelledShell(label: "Run ${tag}", script: "chmod +x gradlew \n" +
                "Xvfb :99 -screen 0 1920x1080x24 & ./gradlew -x test runTaskOnJenkins -PtaskTag=${tag}")
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
