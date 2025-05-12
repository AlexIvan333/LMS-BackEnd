FROM openjdk:21-jdk-slim
VOLUME /tmp
COPY eureka-server/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
