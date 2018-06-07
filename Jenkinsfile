// iubar-auto-update
pipeline {
    agent {    
    	docker {   	
    		image 'iubar-maven-ubuntu'
    		label 'docker'
    		args '-e MAVEN_CONFIG=/home/jenkins/.m2 -v /home/jenkins/.m2:/home/jenkins/.m2:rw,z'
    	} 
    }
    stages {
        stage ('Build') {
            steps {
            	echo 'Building...'
                sh 'mvn -B -DskipTests=true clean package' https://i.stack.imgur.com/wqati.png
            }
        }
		stage('Test') {
            steps {
            	echo 'Testing...'
                sh 'mvn -B -Djava.io.tmpdir=${WORKSPACE}@tmp -Djava.awt.headless=true test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml' // show junit log in Jenkins 
                }
            }
        }
		stage ('Deploy') {
            steps {
            	echo 'Deploying...'
		sh 'cat /home/jenkins/.m2/settings.xml'
		sh 'mvn --version'
		sh 'mvn help:effective-settings'
		echo '...Deploying...' // @see https://i.stack.imgur.com/wqati.png
                // sh 'mvn -X -B -DskipTests=true --global-settings /home/jenkins/.m2/settings.xml --settings /home/jenkins/.m2/settings.xml jar:jar deploy:deploy'
    	        sh 'mvn -X -B -DskipTests=true deploy'
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
            sh "curl -H 'JENKINS: Pipeline Hook Iubar' -i -X GET -G ${env.IUBAR_WEBHOOK_URL} -d status=${currentBuild.currentResult} -d project_name=${JOB_NAME}"
        }
		cleanup {
			cleanWs()
			dir("${env.WORKSPACE}@tmp") {				
				deleteDir()
			}
        }
    }    
}


// 1) If you want to skip tests you can add the following to the command line.
// mvn -DskipTests build
// 2) compiles the tests, but skips running them
// mvn -Dmaven.test.skip=true build
// 3) skips compiling the tests and does not run them
// mvn clean install
