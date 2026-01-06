# Imagen base con JDK 17 para compilar
FROM gradle:8.5-jdk17 AS build

# Directorio de trabajo
WORKDIR /app

# Copiamos archivos de configuracion de Gradle primero (para cache de dependencias)
COPY build.gradle settings.gradle ./

# Copiamos el codigo fuente
COPY src ./src

# Compilamos el proyecto sin ejecutar tests (los tests se corren aparte)
RUN gradle build -x test --no-daemon

# Imagen final mas liviana solo con JRE
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copiamos el JAR generado desde la etapa de build
COPY --from=build /app/build/libs/*.jar app.jar

# Puerto que expone el microservicio
EXPOSE 8091

# Comando para ejecutar la aplicacion
ENTRYPOINT ["java", "-jar", "app.jar"]
