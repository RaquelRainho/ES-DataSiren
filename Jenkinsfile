pipeline {
	agent any

	stages{
            stage('Compile stage'){
                steps{
                    sh 'echo "trying to compile"'
                    sh 'mvn -DskipTests package'
                }
            }
            stage('Deploy stage'){
                steps{
                    sh 'echo "trying to deploy"'
                }
            }
            stage('Run stage'){
                steps{
                    sh 'echo "trying to run"'
                }
            }
	}
}