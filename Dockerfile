FROM openjdk:jdk-alpine
MAINTAINER vipan
COPY ./target/cashman-rest-service-0.1.0.jar cashman-rest-service-0.1.0.jar
CMD ["java","-jar","cashman-rest-service-0.1.0.jar"]
