FROM openjdk:jdk-alpine
MAINTAINER vipan


ENV NEW_RELIC_APP_NAME=Cashman-Test-1

COPY ./target/cashman-rest-service-0.1.0.jar cashman-rest-service-0.1.0.jar
COPY ./entrypoint.sh .

RUN chmod +x entrypoint.sh

CMD ./entrypoint.sh

#ENTRYPOINT ["sh", "entrypoint.sh"]
