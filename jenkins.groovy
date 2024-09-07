task_branch = "${TEST_BRANCH_NAME}"
def branch_cutted = task_branch.contains("origin") ? task_branch.split('/')[1] : task_branch.trim()
currentBuild.displayName = "$branch_cutted"
base_git_url = "https://github.com/Anton-Bobr/for_M_G.git"

node {
    withGradle {
        withEnv(["branch=${branch_cutted}", "base_url=${base_git_url}"]) {
            stage("Checkout Branch") {
                if (!"$branch_cutted".contains("main")) {
                    try {
                        getProject("$base_git_url", "$branch_cutted")
                    } catch (err) {
                        echo "Failed get branch $branch_cutted"
                        throw ("${err}")
                    }
                } else {
                    echo "Current branch is master"
                    scriptExec("java --version")
//                    scriptExec("gradle --version")
                }
            }

            try {
                stage("Run Test") {
                    runTestWithTag("SubscribeToNewUsers")
                }
            } finally {
                stage("Allure") {
                    generateAllure()
                }
            }
        }
    }
}

def runTestWithTag(String tag) {
    try {
        labelledShell(label: "Run ${tag}", script: "chmod +x gradlew \n./gradlew -x test ${tag}")
    } finally {
        echo "some failed tests"
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
