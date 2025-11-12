# Etapa 1: build
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Copia el código y construye el JAR
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: runtime
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copia el JAR construido
COPY --from=builder /app/target/*.jar app.jar

# Expone el puerto (tu app usa 8080)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
