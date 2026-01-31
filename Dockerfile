# Estágio 1: Build (Compilação)
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Compila e gera o .jar (pula testes pra ser mais rápido no deploy)
RUN mvn clean package -DskipTests

# Estágio 2: Run (Execução)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Copia o .jar gerado no estágio anterior
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta 8080
EXPOSE 8080

# Comando para rodar
ENTRYPOINT ["java", "-jar", "app.jar"]