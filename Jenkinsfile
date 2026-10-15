// =============================================================================
// DemoBlaze E2E Automation - Declarative Jenkins Pipeline
// =============================================================================
//
// Required Jenkins plugins:
//   - Pipeline
//   - Docker Pipeline
//   - JUnit (for test result publishing)
//   - Allure Jenkins Plugin (optional, for Allure HTML reports)
//   - Git
//   - Credentials Binding
//
// Required Jenkins configuration:
//   - Docker available on the Jenkins agent
//   - Jenkins credential: (none required for public DemoBlaze - no login to Jenkins Docker registry)
//   - Set JENKINS_URL, JENKINS_USER, JENKINS_API_TOKEN if triggering remotely
//
// =============================================================================

pipeline {

    agent any

    parameters {
        choice(
            name: 'TEST_SUITE',
            choices: [
                'src/test/resources/testng.xml',
                'src/test/resources/testng-smoke.xml',
                'src/test/resources/testng-regression.xml',
                'src/test/resources/testng-e2e.xml'
            ],
            description: 'TestNG suite file to execute'
        )
        choice(
            name: 'BROWSER',
            choices: ['chrome', 'firefox'],
            description: 'Browser to use for test execution'
        )
        booleanParam(
            name: 'HEADLESS',
            defaultValue: true,
            description: 'Run browser in headless mode'
        )
    }

    environment {
        // Prevent concurrent builds to avoid port/resource conflicts
        // Remove this if your agent has isolated Docker networks per build
        COMPOSE_PROJECT_NAME = "demoblaze_${BUILD_NUMBER}"
    }

    options {
        // Prevent parallel builds that would conflict on shared ports
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timeout(time: 45, unit: 'MINUTES')
    }

    stages {

        // -----------------------------------------------------------------------
        stage('Checkout') {
        // -----------------------------------------------------------------------
            steps {
                echo "Checking out branch: ${GIT_BRANCH}"
                checkout scm
            }
        }

        // -----------------------------------------------------------------------
        stage('Validate Configuration') {
        // -----------------------------------------------------------------------
            steps {
                script {
                    echo "=== Build Configuration ==="
                    echo "Suite:    ${params.TEST_SUITE}"
                    echo "Browser:  ${params.BROWSER}"
                    echo "Headless: ${params.HEADLESS}"
                    sh "java -version || true"
                    sh "mvn --version || ./mvnw --version || true"
                    sh "docker --version"
                    sh "docker compose version"
                }
            }
        }

        // -----------------------------------------------------------------------
        stage('Build Automation Image') {
        // -----------------------------------------------------------------------
            steps {
                sh """
                    docker compose -p ${COMPOSE_PROJECT_NAME} build --no-cache test-runner
                """
            }
        }

        // -----------------------------------------------------------------------
        stage('Run Test Suite') {
        // -----------------------------------------------------------------------
            steps {
                sh """
                    TESTNG_SUITE='${params.TEST_SUITE}' \
                    BROWSER='${params.BROWSER}' \
                    HEADLESS='${params.HEADLESS}' \
                    docker compose -p ${COMPOSE_PROJECT_NAME} up \
                        --abort-on-container-exit \
                        --exit-code-from test-runner
                """
            }
            post {
                always {
                    // Capture the test-runner logs even if tests fail
                    sh """
                        docker compose -p ${COMPOSE_PROJECT_NAME} logs test-runner \
                            > target/logs/test-runner-docker.log 2>&1 || true
                    """
                }
            }
        }

        // -----------------------------------------------------------------------
        stage('Publish Test Results') {
        // -----------------------------------------------------------------------
            steps {
                junit(
                    testResults: 'target/surefire-reports/*.xml',
                    allowEmptyResults: false
                )
            }
        }

        // -----------------------------------------------------------------------
        stage('Archive Artifacts') {
        // -----------------------------------------------------------------------
            steps {
                archiveArtifacts(
                    artifacts: 'target/screenshots/**,target/logs/**,target/allure-results/**,target/surefire-reports/**',
                    allowEmptyArchive: true
                )
                // Publish Allure report if the Allure plugin is installed
                script {
                    try {
                        allure([
                            includeProperties: false,
                            jdk: '',
                            results: [[path: 'target/allure-results']]
                        ])
                    } catch (e) {
                        echo "Allure plugin not available – skipping HTML report: ${e.message}"
                    }
                }
            }
        }

    } // end stages

    post {

        // -----------------------------------------------------------------------
        always {
            // -----------------------------------------------------------------------
            script {
                echo "=== Cleanup: stopping and removing Docker Compose services ==="
                sh """
                    docker compose -p ${COMPOSE_PROJECT_NAME} down --volumes --remove-orphans || true
                    docker compose -p ${COMPOSE_PROJECT_NAME} rm -f || true
                """
            }
        }

        success {
            echo "=== PIPELINE PASSED: All tests green ==="
        }

        failure {
            echo "=== PIPELINE FAILED: Check test results and logs above ==="
        }

        unstable {
            echo "=== PIPELINE UNSTABLE: Some tests failed ==="
        }

    }

}
