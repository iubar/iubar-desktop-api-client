pipeline {
    agent {    
    	docker {    		
    		image 'iubar-maven-alpine'
    		label 'docker'
    		args '-e MAVEN_CONFIG=/home/jenkins/.m2 -v $HOME/.m2:/home/jenkins/.m2:rw,z'
    	} 
    }
    stages {
        stage ('Build') {
            steps {
            	echo 'Building...'
                sh 'mvn -B -DskipTests=true clean package'
            }
        }
		stage('Test') {
            steps {
            	echo 'Testing...'
                sh 'mvn -B test -Djava.io.tmpdir=${WORKSPACE}@tmp'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml' // show junit log in Jenkins 
                }
            }
        }                      
        stage('Analyze') {
            steps {
                sh 'sonar-scanner'
            }
        }
    }
	post {
        changed {
        	echo "CURRENT STATUS: ${currentBuild.currentResult}"
            sh "curl -G -v \"https://api.telegram.org/bot447819318:AAHHaIsNBxD1GhHeag4xRIXh6rCp9EV5S_c/sendMessage?chat_id=-1001181492268&parse_mode=HTML\" --data \"text=<b>JENKINS</b>%0A<i>${JOB_NAME}</i>%0AStatus%20is%20changed%20to%20${currentBuild.currentResult}\""
        }
		cleanup {
			deleteDir()
			dir("${env.WORKSPACE}@tmp") {
				echo 'Cleaning ${env.WORKSPACE}@tmp'
			  deleteDir()
			}
        }
    }    
}