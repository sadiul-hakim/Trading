# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:21-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the Maven build file and source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN ./mvnw package -DskipTests

# Expose the application port
EXPOSE 9095

# Run the application
CMD ["java", "-jar", "target/MyTrading-1.0.0.jar"]
