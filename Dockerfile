# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Define build-time arguments
ARG SSL_KEY_STORE_PASSWORD
ARG APP_PORT

# Set runtime environment variables
ENV UID="1000"
ENV GID="1000"
ENV SSL_KEY_STORE_PASSWORD=${SSL_KEY_STORE_PASSWORD}
ENV APP_PORT=${APP_PORT}

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

# Copy the keystore file
COPY keystore.jks /app/ca/keystore.jks


## Copy the certificate file
#COPY ca.cer /app/certs/ca.cer
#
## Import the certificate into the Java cacerts keystore
#RUN keytool -importcert -alias pp-backend-ca -file /app/certs/ca.cer -cacerts -storepass ${SSL_KEY_STORE_PASSWORD} -noprompt
#
## Copy the cacerts keystore to the app directory and rename it to keystore.jks
#RUN cp $(dirname $(dirname $(readlink -f $(which java))))/lib/security/cacerts /app/certs/keystore.jks


# Switch to the new user
USER builder

# Make the application port available to the world outside this container
EXPOSE ${APP_PORT}

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app/pp_backend-0.0.1-SNAPSHOT.jar"]
