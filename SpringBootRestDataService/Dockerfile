#Construcción
FROM eclipse-temurin:21.0.6_7-jdk-alpine-3.21 AS builder-data
WORKDIR /app
COPY pom.xml ./
COPY .mvn ./.mvn
COPY mvnw ./
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw clean package -DskipTests

#Imagen de ejecución
FROM eclipse-temurin:21.0.6_7-jdk-alpine-3.21
WORKDIR /app
EXPOSE 8080

# Copiar solo el archivo JAR generado
COPY --from=builder-data /app/target/SpringBootRest-0.0.1-SNAPSHOT.jar /app/SpringBootRest.jar
ENTRYPOINT ["java", "-jar", "/app/SpringBootRest.jar"]
