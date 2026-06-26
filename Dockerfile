FROM gradle:8.10.2-jdk21 AS build

WORKDIR /app

COPY --chown=gradle:gradle . .

RUN gradle installDist --no-daemon


FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/build/install/warehouse ./

CMD ["./bin/warehouse"]
