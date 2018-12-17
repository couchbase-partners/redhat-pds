#docker build -t ezeev/twitter-streamer:release-1.0 .
FROM maven:3.3-jdk-8-onbuild

FROM openjdk:8-jdk-alpine
COPY --from=0 /usr/src/app/target/twitter-streamer-1.0.jar /opt/app.jar
EXPOSE 8081
CMD ["java","-jar","/opt/app.jar"]