# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set environment variables for UID and GID
ENV UID="1000"
ENV GID="1000"

# Create a group and user with the specified UID and GID
RUN addgroup -g "${GID}" builder && \
    adduser -u "${UID}" -G builder -s /bin/sh -D builder

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file into the container at /app
COPY target/pp_backend-0.0.1-SNAPSHOT.jar /app/pp_backend-0.0.1-SNAPSHOT.jar

# Create a writable directory for certificates
RUN mkdir -p /app/certificates && \
    chown -R builder:builder /app

# Switch to the new user
USER builder

# Make port 8081 available to the world outside this container
EXPOSE 8081

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app/pp_backend-0.0.1-SNAPSHOT.jar"]
