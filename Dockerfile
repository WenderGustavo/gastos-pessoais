FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /build/target/*.jar ./gastos.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "gastos.jar"]
