# Use the official OpenJDK image as base
FROM amazoncorretto:11.0.17

# Set working directory
WORKDIR /app

# Copy the JAR file into the container
COPY target/seller-app.jar seller-app.jar

# Run the Spring Boot application
ENTRYPOINT ["java","-jar","seller-app.jar"]

EXPOSE 8080

# Define the command to run when the container starts
#CMD ["java", "-jar", "app.jar"]
