pipeline {
    agent{node('master')}
    stages { 
        stage('Dowload project') {
            steps { 
                script {cleanWs()}
                script {
                    echo 'Start boot process...'
                    checkout([$class                           : 'GitSCM',
                              branches                         : [[name: '*/master']],
                              doGenerateSubmoduleConfigurations: false,
                              extensions                       : [[$class           : 'RelativeTargetDirectory',
                                                                   relativeTargetDir: 'GitDir']],
                              submoduleCfg                     : [],
                              userRemoteConfigs                : [[credentialsId: 'LizaBalabanovaGit', url: 'https://github.com/Balabanova/jenkins_docker.git']]])
                }
            }
        }     
        
         stage ('Create docker image'){
            steps{
                script{
                    sh "docker build ${WORKSPACE}/GitDir -t webapp"
                    sh "docker run -d webapp"
                    sh "docker exec -it webapp "df -h > ~/proc""
                }
            }
        }
        
    }   
}
