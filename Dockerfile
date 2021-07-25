FROM hseeberger/scala-sbt:graalvm-ce-21.0.0-java11_1.4.7_2.13.4 AS builder
WORKDIR /app
COPY . .
RUN sbt stage

FROM openjdk:8-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/universal/stage .
ENTRYPOINT ["bin/nasa-rover"]