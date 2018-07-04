pipeline {
    agent {    
    	docker {   	
    		image 'iubar-maven-alpine'
    		label 'docker'
    		args '-v /home/jenkins/.m2:/home/jenkins/.m2:rw,z'
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
                sh 'mvn -B -Djava.io.tmpdir=${WORKSPACE}@tmp -Djava.awt.headless=true test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml' // show junit log in Jenkins 
                }
            }
        }
        stage('Analyze') {
            steps {
				script {
					try {
						sh 'sonar-scanner'
					} catch (err){
						echo "SonarQube: analyze failed !!!"
					}
				}
            }
        }		
        stage('Quality gate') {
            steps {
				sh '''
				    QUALITYGATE=$(curl http://192.168.0.117:9000/api/qualitygates/project_status?projectKey=java%3Aiubar-desktop-api-client | jq '.projectStatus.status')
				    QUALITYGATE=$(echo "$QUALITYGATE" | sed -e 's/^"//' -e 's/"$//')
				    echo "QUALITYGATE: ${QUALITYGATE}"
                    if [ $QUALITYGATE = OK ]; then
                       echo "High five !"
                    else
                       echo "Poor quality !"
                       exit 1
                    fi				    
				'''
            }
        }
		stage ('Deploy') {
            steps {
            	echo 'Deploying...'
                sh 'mvn -B -DskipTests=true deploy'
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

