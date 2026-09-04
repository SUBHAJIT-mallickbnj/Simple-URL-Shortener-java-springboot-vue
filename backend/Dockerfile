FROM maven:3.9.11-eclipse-temurin-21 AS build

WORKDIR /app
COPY backend/pom.xml .
COPY backend/.mvn .mvn
COPY backend/mvnw .
COPY backend/mvnw.cmd .
RUN chmod +x mvnw
COPY backend/src src
RUN ./mvnw -B clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app
COPY --from=build /app/target/linknest-api-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]