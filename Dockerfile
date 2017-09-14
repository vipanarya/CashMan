FROM openjdk
MAINTAINER vipan
COPY gs-rest-service-0.1.0.jar gs-rest-service-0.1.0.jar
CMD ["java","-jar","gs-rest-service-0.1.0.jar"]