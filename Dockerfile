# Etapa de build
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app

# Copia los archivos del proyecto
COPY . .

# Compila el proyecto, omitiendo los tests
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copia el jar desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Expone el puerto 8080
EXPOSE 8080

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]