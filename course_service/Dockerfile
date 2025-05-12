FROM openjdk:21-jdk-slim
ARG BUILD_DATE
ENV BUILD_DATE=$BUILD_DATE
VOLUME /tmp
COPY course_service/build/libs/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
