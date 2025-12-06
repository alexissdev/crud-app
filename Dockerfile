# Use official OpenJDK 17 slim image
FROM eclipse-temurin:17

# Set working directory in the container
WORKDIR /app

# Copy the JAR file to the container
COPY build/libs/crud-app.jar app.jar

# Expose the port your app runs on
EXPOSE 8080

# Command to run the app
ENTRYPOINT ["java","-jar","app.jar"]
