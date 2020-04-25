pipeline {
	agent any

	stages{
            stage('Build stage'){
                agent{
                    dockerfile{
                        args '-v "$PWD":/usr/src/mymaven -v "$HOME/.m2":/root/.m2 -v "$PWD/target:/usr/src/mymaven/target" -w /usr/src/mymaven'
                    }
                }
                steps{
                    sh 'echo "trying to build"'
                    sh 'ls -la /root/.m2'
                    sh 'mvn -DskipTests package'
                    sh 'ls -la target'
                }
            }
            stage('Deploy (to JFrog) stage'){
                agent {
                    dockerfile {
                        args '-v "$PWD":/usr/src/mymaven -v "$HOME/.m2":/root/.m2 -w /usr/src/mymaven'
                    }
                }
                steps{
                    sh 'echo "trying to deploy"'
                    sh 'mvn deploy'
                }
            }
            stage('Run stage'){
                steps{
                    sh 'echo "trying to run"'
                }
            }
	}
}