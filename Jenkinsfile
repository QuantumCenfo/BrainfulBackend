pipeline {
	agent { 
		docker { 
			image 'gradle:8.10.2-jdk21'
			args '--mount source=gradle-cache,target=/home/gradle/.gradle'
		}
	}
	options { timestamps() }
	environment { CI = 'true' }
	stages {
		stage('Prep Gradle cache'){ steps { sh 'mkdir -p "$WORKSPACE/.gradle"' } }
		stage('Build & Test') {
			steps {
				sh 'chmod +x gradlew'
				sh './gradlew clean test jacocoTestReport --no-daemon'
			}
			post {
				always {
					junit 'build/test-results/test/*.xml'
					recordCoverage(
						tools: [
							jacoco(pattern: 'build/reports/jacoco/test/jacocoTestReport.xml')
						],
						sourceCodeRetention: 'NEVER',
						failOnError: false,
						calculateDiffForChangeRequests: false
					)
				}
			}
		}
		stage('SonarCloud') {
			steps {
				withSonarQubeEnv('SonarCloud') {
					sh './gradlew sonarqube --no-daemon -Dsonar.host.url=$SONAR_HOST_URL -Dsonar.token=$SONAR_AUTH_TOKEN'
				}
			}
		}
	}
}


