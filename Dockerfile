FROM gradle:jdk17-alpine AS builder

WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . /home/gradle/src
RUN gradle build --no-daemon
RUN ls -la /home/gradle/src/build/libs

FROM openjdk:17-alpine

EXPOSE 8080
WORKDIR /app
COPY --from=builder /home/gradle/src/build/libs/market-making-service-0.0.1-SNAPSHOT-all.jar /app/application.jar

ENTRYPOINT ["java","-XX:+UnlockExperimentalVMOptions","-Djava.security.egd=file:/dev/./urandom","-jar","/app/application.jar"]
