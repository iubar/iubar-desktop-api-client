pipeline {
    agent {    
    	docker {   	
    		image 'iubar-maven-alpine'
    		label 'docker'
    		args '-v $HOME/.m2:/home/jenkins/.m2:rw,z -v $HOME/.sonar:/home/jenkins/.sonar:rw,z'
    	} 
    }
    stages {
        stage ('Build') {
            steps {
                sh 'mvn -B -DskipTests=true clean package'
            }
        }
		stage('Test') {
            steps {
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
            environment { 
				SONAR_PROJECTKEY = 'java:iubar-desktop-api-client'
            }		
            steps {
				sh '''
				    QUALITYGATE=$(curl http://192.168.0.117:9000/api/qualitygates/project_status?projectKey=$SONAR_PROJECTKEY | jq '.projectStatus.status')
				    QUALITYGATE=$(echo "$QUALITYGATE" | sed -e 's/^"//' -e 's/"$//')
				    echo "QUALITYGATE: ${QUALITYGATE}"
                    if [ $QUALITYGATE = OK ]; then
                       echo "High five !"
                    else
                       echo "Poor quality !"
					   echo "( see http://192.168.0.117:9000/dashboard?id=$SONAR_PROJECTKEY)"
                       exit 1
                    fi				    
				'''
            }
        }
		stage ('Deploy') {
            steps {
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

