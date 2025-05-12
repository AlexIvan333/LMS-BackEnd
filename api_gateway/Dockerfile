FROM openjdk:21-jdk-slim
ARG BUILD_DATE
ENV BUILD_DATE=$BUILD_DATE
VOLUME /tmp
COPY api_gateway/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]