# =============================================================================
# DemoBlaze E2E Automation - Test Runner Dockerfile
# Pinned to Eclipse Temurin JDK 21 on Maven 3.9
# =============================================================================
FROM maven:3.9.6-eclipse-temurin-21

# Set working directory
WORKDIR /app

# Pre-cache Maven dependencies layer
COPY pom.xml ./
RUN mvn dependency:go-offline -B -DskipTests

# Copy framework source code and test resources
COPY src ./src
COPY testng.xml* ./

# Default entrypoint runs Maven clean test against remote grid
ENTRYPOINT ["mvn", "test", "-B"]
CMD ["-Dremote=true", "-Dgrid.url=http://selenium-chrome:4444/wd/hub", "-Dheadless=true"]
