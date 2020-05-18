pipeline {
	agent any
        environment {
            CI = 'true'
        }
	stages{
            stage('Build stage'){
                agent{
                    dockerfile{
                        filename 'Dockerfile-build'
                        args '-v "$PWD":/usr/src/mymaven -v "$HOME/.m2":/root/.m2 -v "$PWD/target:/usr/src/mymaven/target" -w /usr/src/mymaven'
                    }
                }
                steps{
                    sh 'echo "trying to build"'
                    sh 'cp /root/settings.xml /root/.m2/settings.xml'
                    sh 'ls -la /root/.m2'
                    sh 'mvn -DskipTests package'
                    sh 'ls -la target'
                }
            }

            stage('Deploy (to JFrog) stage'){
                agent {
                    dockerfile {
                        filename 'Dockerfile-build'
                        args '-v "$PWD":/usr/src/mymaven -v "$HOME/.m2":/root/.m2 -w /usr/src/mymaven'
                    }
                }
                steps{
                    sh 'echo "trying to deploy"'
                    sh 'ls -la /root/.m2'
                    sh 'ls -la'
                    sh 'cat pom.xml'
                    sh 'cat /root/.m2/settings.xml'
                    sh 'mvn -DskipTests deploy'
                }
            }
            stage('Cleanup stage'){
                steps{
                    sshagent(credentials: ['esp24']){
                        sh "ssh -o 'StrictHostKeyChecking=no' -l esp24 192.168.160.103 docker stop esp24-datasiren || true"
                        sh "ssh -o 'StrictHostKeyChecking=no' -l esp24 192.168.160.103 docker rm esp24-datasiren || true"
                    }
                }
            }
            stage('Docker image build stage'){
                steps{
                    sh 'ls -la'
                    sh 'pwd'
                    sh 'ls /'
                    script {
                        dockerImage = docker.build("esp24-datasiren"+ ":$BUILD_NUMBER")
                    }
                }
            }
            stage('Docker image deploy stage') {
                steps{
                    script {
                        docker.withRegistry( 'http://192.168.160.99:5000') {
                            dockerImage.push("latest")
                        }
                    }
                }
            }
            stage('Run stage'){
                steps{
                    sh 'echo "trying to run"'
                    sshagent(credentials: ['esp24']){
                        sh "ssh -o 'StrictHostKeyChecking=no' -l esp24 192.168.160.103 uname -a"
                        sh "scp /var/jenkins_home/workspace/esp24-dataSiren/Data/* esp24@192.168.160.103:/home/esp24"
                        sh "ssh -o 'StrictHostKeyChecking=no' -l esp24 192.168.160.103 python3 DataRead.py &"
                        sh "ssh -o 'StrictHostKeyChecking=no' -l esp24 192.168.160.103 docker pull 192.168.160.99:5000/esp24-datasiren"
                        sh "ssh -o 'StrictHostKeyChecking=no' -l esp24 192.168.160.103 docker run -d -p 24010:8080 -p 24848:4848 --name esp24-datasiren 192.168.160.99:5000/esp24-datasiren"
                    }
                }
            }

            stage('Test stage'){
                when{
                    branch 'testing'
                }
                steps{
                    sshagent(credentials: ['esp24']){
                        timeout(120){
                            waitUntil{
                                script{
                                    def r = sh script: "wget -q http://192.168.160.103:24010/datasiren-0.0.4/home", returnStdout:true
                                    return (r==0);

                                }
                            }
                        }
                    }
                    sh 'echo "running tests"'
                    sh 'mvn test'
                    
                    sshagent(credentials: ['esp24']){
                        sh "ssh -o 'StrictHostKeyChecking=no' -l esp24 192.168.160.103 docker stop esp24-datasiren || true"
                    }
                }
            }
	}
}