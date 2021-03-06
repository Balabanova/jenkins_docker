pipeline {
    agent{node('master')}
    stages { 
        stage('Download git repository') {
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
                            print 'problem'
                            currentBuild.result = 'FAILURE'
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

                        sh "echo '${password}' | sudo -S docker build ${WORKSPACE}/auto -t v_docker"
                        sh "echo '${password}' | sudo -S docker run -d -p 8008:80 --name v_name -v /home/adminci/v_dir:/stat v_docker"
                    }
                    //sh "docker build ${WORKSPACE}/GitDir -t webapp"
                    //sh "docker run -d webapp"
                    //sh "docker exec -it webapp "df -h > ~/proc""
                }
            }
        }      
    
    stage ('Write info to file'){
            steps{
                script{
                    withCredentials([
                        usernamePassword(credentialsId: 'srv_sudo',
                        usernameVariable: 'username',
                        passwordVariable: 'password')
                    ]) {
                        //sh "echo '${password}' | sudo -S docker exec -t v_name bash -c 'stat text_file.txt > /stat/v_file.txt'"
                        sh "echo '${password}' | sudo -S docker exec -t v_name bash -c 'df -h > /stat/v_file.txt'"
                        sh "echo '${password}' | sudo -S docker exec -t v_name bash -c '\n top -n 1 -b >> /stat/v_file.txt'"                       
                    }
                }
            }
        }
        
        stage ('Stop'){
            steps{
                script{
                    withCredentials([
                        usernamePassword(credentialsId: 'srv_sudo',
                        usernameVariable: 'username',
                        passwordVariable: 'password')
                    ]) {
                        sh "echo '${password}' | sudo -S docker stop v_name"                 
                    }
                }
            }
        }
        
    }
}
