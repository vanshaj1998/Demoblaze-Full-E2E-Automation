# Docker Execution Guide – DemoBlaze E2E Automation

This directory details how to run the DemoBlaze automated test suite in isolated Docker containers using Docker Compose.

---

## Architecture Overview

The containerized setup uses a two-service decoupled architecture:

```
+----------------------------------------------------------------+
|                         Docker Bridge                          |
|                                                                |
|  +------------------------+        +------------------------+  |
|  |      test-runner       |        |    selenium-chrome     |  |
|  |  (Maven 3.9 + JDK 21)  | =====> | (Standalone Chrome)    |  |
|  |  Runs TestNG suite     |  HTTP  | Port 4444: WebDriver   |  |
|  +------------------------+        | Port 7900: NoVNC UI    |  |
|             |                      +------------------------+  |
+-------------|--------------------------------------------------+
              | (Volume mount)
              v
       ./target/ (Host)
       - target/surefire-reports/
       - target/allure-results/
       - target/screenshots/
       - target/logs/
```

1. **`selenium-chrome`**:
   - Official `selenium/standalone-chrome:4.18.1` image.
   - Allocated `shm_size: 2gb` to eliminate Chrome renderer crashes due to shared memory starvation.
   - Includes health check (`curl -f http://localhost:4444/wd/hub/status`).
   - Includes NoVNC server on port `7900` (view live browser execution at `http://localhost:7900`, password: `secret`).

2. **`test-runner`**:
   - Built from repository `Dockerfile` using `maven:3.9.6-eclipse-temurin-21`.
   - Waits for `selenium-chrome` to pass health check before initiating Maven tests.
   - Volume mounts `./target:/app/target` ensuring all test outputs survive container termination.

---

## Quickstart Commands

### 1. Run Complete Test Suite
```bash
docker compose up --build --abort-on-container-exit --exit-code-from test-runner
```
*The `--exit-code-from test-runner` flag ensures CI systems receive the accurate pass/fail exit code from the Maven process.*

### 2. Run Specific TestNG Group (e.g. Smoke Only)
```bash
docker compose run --rm test-runner -Dgroups=smoke
```

### 3. Run Specific Suite (e.g. E2E Only)
```bash
docker compose run --rm test-runner -Dgroups=e2e
```

### 4. Tear Down Containers and Volumes
```bash
docker compose down -v --remove-orphans
```

---

## Environment Variables Reference

| Variable | Default Value | Description |
|---|---|---|
| `REMOTE` | `true` | Enables RemoteWebDriver connection |
| `GRID_URL` | `http://selenium-chrome:4444/wd/hub` | Selenium Grid endpoint |
| `BROWSER` | `chrome` | Browser engine |
| `HEADLESS` | `true` | Headless execution flag |
| `BASE_URL` | `https://www.demoblaze.com/` | Application URL under test |
| `EXPLICIT_WAIT_SECONDS` | `15` | Default explicit wait timeout |
| `PAGE_LOAD_TIMEOUT_SECONDS`| `30`| Page load wait timeout |

---

## Troubleshooting

- **Connection Refused to Grid**: Ensure `selenium-chrome` container is healthy before runner starts (`condition: service_healthy`).
- **Chrome Crash / Session Not Created**: Ensure `shm_size: 2gb` is present in docker-compose.yml to avoid `/dev/shm` buffer overflows.
- **Permission issues on target/**: In Linux CI, ensure the host directory mounted to `./target` has write permissions for the Docker user.
