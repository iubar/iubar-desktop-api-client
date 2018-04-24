pipeline {
    agent {    
    	docker {    		
    		image 'iubar/iubar-maven-alpine'
    		label 'docker'
    		args '-v $HOME/.m2:/root/.m2'
    	} 
    }
    stages {
        stage ('Initialize') {
            steps {
            	echo 'Initializing...'
            	// Since you're SSH'ing into the server instead of using an actual interactive shell, the $USER variable is (and several other environment variables are) never set.
                echo 'USER = $(whoami)'
				sh 'printenv'
				echo 'Hello, Maven'
                sh 'mvn --version'
            }
        }
        stage ('Build') {
            steps {
            	echo 'Building...'
                sh 'mvn -B -DskipTests=true clean package'
            }
        }
		stage('Test') {
            steps {
            	echo 'Testing...'
                sh 'mvn -B test'
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
}