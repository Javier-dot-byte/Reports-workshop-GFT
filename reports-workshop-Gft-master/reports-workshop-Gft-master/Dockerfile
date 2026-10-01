FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -q

COPY src ./src
RUN mvn package -DskipTests -q

FROM eclipse-temurin:21-jre-alpine AS runtime

RUN addgroup -S reporting && adduser -S reporting -G reporting

WORKDIR /app

COPY --from=builder /app/target/reporting-*.jar app.jar

RUN chown reporting:reporting app.jar

USER reporting

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]