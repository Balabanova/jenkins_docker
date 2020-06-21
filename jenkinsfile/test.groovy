pipeline {
    agent{node('master')}
    stages { 
        stage('Dowload project') {
            steps { 
                script {
                    cleanWs()
                    withCredentials([
                        usernamePassword(credentialsId: 'srv_sudo',
                        usernameVariable: 'username',
                        passwordVariable: 'password')
                    ]) {
                        try {
                            sh "echo '${password}' | sudo -S docker stop v_name"
                            sh "echo '${password}' | sudo -S docker container rm v_name"
                        } catch (Exception e) {
                            print 'container not exist, skip clean'
                        }
                    }
                }
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
        
         stage ('Create docker image'){
            steps{
                script{
                    withCredentials([
                        usernamePassword(credentialsId: 'srv_sudo',
                        usernameVariable: 'username',
                        passwordVariable: 'password')
                    ]) {

                        sh "echo '${password}' | sudo -S docker build ${WORKSPACE}/auto -t v_nginx"
                        sh "echo '${password}' | sudo -S docker run -d -p 8008:80 --name v_name -v /home/adminci/v_dir:/stat v_nginx"
                    }
                    //sh "docker build ${WORKSPACE}/GitDir -t webapp"
                    //sh "docker run -d webapp"
                    //sh "docker exec -it webapp "df -h > ~/proc""
                }
            }
        }      
    
    stage ('Write to file'){
            steps{log
                script{
                    withCredentials([
                        usernamePassword(credentialsId: 'srv_sudo',
                        usernameVariable: 'username',
                        passwordVariable: 'password')
                    ]) {
                        
                        sh "echo '${password}' | sudo -S docker exec -t v_name bash -c 'df -h > /stat/text_file.txt'"
                        sh "echo '${password}' | sudo -S docker exec -t v_name bash -c 'top -n 1 -b >> /stat/text_file.txt'"
                    }
                }
            }
        }
    }
}
