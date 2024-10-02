def pipelinesNameList = ["SubscribeToNewUsers", "ComparisonFollowers", "Unsubscribing"] as String[]
//def pipelinesNameList = ["test1", "test2"] as String[]
def pipelineForStart

pipeline {
    agent any
    triggers {
        cron('H/40 6-13 * * *')
//        cron('*/1 * * * *')
    }
    stages {
        stage('Select Pipeline for start') {
            steps {
                script {
                    pipelineForStart = pipelinesNameList.find { pipelineName -> pickFirstUncompletedPipelineToday(pipelineName) }
                }
            }
        }
        stage('Run Pipeline') {
            steps {
                script {
                    echo pipelineForStart
                    if (pipelineForStart) {
                        build job: pipelineForStart, wait: false
                    } else {
                        echo 'Not jobs for start'
                    }
                }
            }
        }
    }
}

def pickFirstUncompletedPipelineToday(String pipelineName) {
    def job = Jenkins.instance.getItemByFullName(pipelineName)
    def today = new Date().clearTime()

    def buildsToday = job.getBuilds().findAll { build ->
        def buildDate = new Date(build.getTimeInMillis()).clearTime()
        return buildDate == today
    }

    if (buildsToday) {
        return buildsToday[0].result.toString() != 'SUCCESS'
    } else {
        return true
    }
}
