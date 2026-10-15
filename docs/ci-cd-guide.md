# CI/CD Orchestration Guide – DemoBlaze E2E Automation

This document explains the end-to-end integration between GitHub Actions, the Jenkins Declarative Pipeline, and Dockerized test execution.

---

## 1. CI/CD Architecture Flow

```
[ Developer / Pull Request / Push ]
                |
                v
    [ GitHub Actions Workflow ]
    (.github/workflows/trigger-jenkins.yml)
                |
                | (Authenticated REST API Call via GitHub Secrets)
                v
       [ Jenkins Controller ]
       (Declarative Jenkinsfile)
                |
                v
     [ Docker Compose Execution ]
   +------------------------------------+
   | 1. Start selenium/standalone-chrome|
   | 2. Await Selenium Health Check OK  |
   | 3. Run test-runner Container       |
   | 4. Collect Reports to host target/ |
   | 5. Teardown Containers (always)    |
   +------------------------------------+
                |
                v
  [ Report & Artifact Publishing ]
   - JUnit XML Results
   - Allure Interactive Report
   - Screenshots Archive (target/screenshots/)
   - Execution Log Archive (target/logs/)
                |
                v
     [ Status Feedback to PR ]
```

---

## 2. Jenkins Setup & Configuration

### Prerequisites & Plugins
Ensure the following plugins are installed on your Jenkins instance:
- **Pipeline:** Declarative Pipeline support.
- **Docker Pipeline / Docker Compose:** For running containerized stages.
- **Allure Jenkins Plugin:** For rendering Allure dashboards directly in the Jenkins UI.
- **JUnit Plugin:** For publishing test trend graphs.
- **Credentials Binding Plugin:** For accessing secret tokens securely.

### Jenkins Pipeline Parameters
The declarative pipeline supports the following build parameters:
- `TEST_GROUP`: Options: `smoke`, `regression`, `e2e`, or `all` (default: `smoke`).
- `BROWSER`: `chrome` or `firefox` (default: `chrome`).
- `HEADLESS`: `true` or `false` (default: `true`).

---

## 3. GitHub Actions to Jenkins Trigger Configuration

The workflow `.github/workflows/trigger-jenkins.yml` triggers the Jenkins pipeline securely via webhook/REST API when commits are pushed or pull requests are opened.

### Required GitHub Secrets
In your GitHub Repository (`Settings -> Secrets and variables -> Actions`):

| Secret Name | Description | Example |
|---|---|---|
| `JENKINS_URL` | Base HTTPS URL of your Jenkins master | `https://jenkins.example.com` |
| `JENKINS_USER` | Authorized Jenkins username | `automation-bot` |
| `JENKINS_API_TOKEN` | Jenkins user API token (generated in Jenkins user profile) | `11a5bc...` |
| `JENKINS_JOB_NAME` | Full name of the parameterized Jenkins pipeline job | `demoblaze-e2e-pipeline` |

### Security & Fork Protection
- GitHub Actions Secrets are **never** accessible to pull requests submitted from public repository forks.
- Jenkins execution credentials are kept within the Jenkins credential store (`demoblaze-app-credentials`), completely decoupled from GitHub.
