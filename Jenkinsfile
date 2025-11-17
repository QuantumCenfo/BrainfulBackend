pipeline {
	agent { 
		docker { 
			image 'gradle:8.10.2-jdk21'
			args "-v ${env.WORKSPACE}/.gradle:/home/gradle/.gradle"
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
					publishCoverage adapters: [jacocoAdapter('build/reports/jacoco/test/jacocoTestReport.xml')], sourceFileResolver: sourceFiles('STORE_ALL_BUILD')
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
		stage('Quality Gate') {
			steps {
				timeout(time: 15, unit: 'MINUTES') {
				waitForQualityGate abortPipeline: true
				}
			}
		}
	}
}


