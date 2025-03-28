FROM openjdk:21-jdk-slim
VOLUME /tmp
COPY build/libs/*.jar app.jar
EXPOSE 8444
ENTRYPOINT ["java", "-jar", "app.jar"]