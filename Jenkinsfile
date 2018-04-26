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
            sh "curl -i -X GET ${env.TELEGRAM_URL}\\&text=JENKINS:%20Status%20of%20${JOB_NAME}%20is%20changed%20to%20${currentBuild.currentResult}"
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