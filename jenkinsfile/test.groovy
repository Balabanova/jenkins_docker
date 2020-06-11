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
                                                                   relativeTargetDir: 'auto']],
                              submoduleCfg                     : [],
                              userRemoteConfigs                : [[credentialsId: 'LizaBalabanovaGit', url: 'https://github.com/Balabanova/jenkins_docker.git']]])
                }
            }
        }     
        
    }   
}
