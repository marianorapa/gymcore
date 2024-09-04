# Use a Gradle image to build the application
FROM gradle:7.4.2-jdk17 AS build

# Set the working directory in the build container
WORKDIR /home/gradle/project

# Copy the Gradle wrapper and build files to the container
COPY gradle /home/gradle/project/gradle
COPY gradlew /home/gradle/project/
COPY build.gradle /home/gradle/project/
COPY settings.gradle /home/gradle/project/

# Copy the project files to the container
COPY . /home/gradle/project/

# Build the application
RUN ./gradlew build -x test

# Use an official OpenJDK runtime as the base image for the final stage
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the built jar file from the build container
COPY --from=build /home/gradle/project/build/libs/gymcore-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the port that your application runs on
EXPOSE 8080

# Set the entry point to run the jar file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
