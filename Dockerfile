# Stage 1: Build the application
# Use base image with Java 17 and gradle
FROM gradle:8.6.0-jdk17-alpine AS build

# Set the working directory in the container
WORKDIR /app

# Copy the Gradle files and dependencies
COPY build.gradle .
COPY settings.gradle .

# Copy the application source code
COPY src ./src

# Build the application
RUN gradle build

# Stage 2: Run the application
# Smaller base for runtime
FROM openjdk:17-alpine

# Set the working directory in the container
WORKDIR /app

# Copy only the built JAR file from the build stage
COPY --from=build /app/build/libs/qrcode-generator-0.0.1-SNAPSHOT.jar ./qrcode-generator-aws.jar

# Expose the app port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "qrcode-generator-aws.jar"]