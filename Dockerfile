FROM openjdk:jdk-alpine
MAINTAINER vipan

ENV NEW_RELIC_LICENSE_KEY=4c6679683e4379914831638f2d71dc102e0f5381
ENV NEW_RELIC_APP_NAME=Cashman-Test-1

COPY ./target/cashman-rest-service-0.1.0.jar cashman-rest-service-0.1.0.jar
COPY ./entrypoint.sh .

RUN chmod +x entrypoint.sh

CMD ["/entrypoint.sh"]
