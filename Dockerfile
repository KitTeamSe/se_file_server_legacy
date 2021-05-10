# SE FILE SERVER DOCKERFILE

# Start with a base image containing Java runtime
FROM openjdk:11

# Add Author info
LABEL maintainer="se@kumoh.ac.kr"

# Add a volume to
VOLUME /var/se-file-server

# Make port 8075 available to the world outside this container
EXPOSE 8075

# The application's jar file
ARG JAR_FILE=build/libs/se_file_server-0.0.1-SNAPSHOT.jar

# Add the application's jar to the container
ADD ${JAR_FILE} run-se-file-server.jar

# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/run-se-file-server.jar"]