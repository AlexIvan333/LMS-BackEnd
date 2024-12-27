FROM openjdk:21-jdk-slim
VOLUME /tmp
COPY build/libs/backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8443
ENTRYPOINT ["java", "-jar", "app.jar"]