# =============================================================================
# Dockerfile Multi-stage Optimizado para Microservice1
# Incluye mejoras de seguridad, caché y JVM tuning
# =============================================================================

# -----------------------------------------------------------------------------
# STAGE 1: Build - Compilación con Gradle
# -----------------------------------------------------------------------------
FROM gradle:8.5-jdk17 AS build

# Metadata
LABEL maintainer="prueba-tecnica"
LABEL description="Microservice1 - Gestión de Clientes"

WORKDIR /app

# Copiar archivos de configuración primero (mejor caché de dependencias)
COPY build.gradle settings.gradle ./

# Descargar dependencias (capa cacheada si no cambian las dependencias)
RUN gradle dependencies --no-daemon || true

# Copiar código fuente
COPY src ./src

# Compilar aplicación (sin tests - se ejecutan en CI/CD)
RUN gradle build -x test --no-daemon

# -----------------------------------------------------------------------------
# STAGE 2: Runtime - Imagen final optimizada
# -----------------------------------------------------------------------------
FROM eclipse-temurin:17-jre-alpine

# Metadata
LABEL maintainer="prueba-tecnica"
LABEL description="Microservice1 - Runtime"

# Crear usuario no-root por seguridad
RUN addgroup -S spring && adduser -S spring -G spring

# Instalar curl para healthchecks
RUN apk add --no-cache curl

WORKDIR /app

# Copiar JAR desde etapa de build
COPY --from=build /app/build/libs/*.jar app.jar

# Cambiar ownership al usuario spring
RUN chown -R spring:spring /app

# Usar usuario no-root
USER spring:spring

# Puerto expuesto
EXPOSE 8091

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8091/actuator/health || exit 1

# Configuración JVM optimizada para contenedores
ENV JAVA_OPTS="-XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:InitialRAMPercentage=50.0 \
    -XX:+UseG1GC \
    -XX:+UseStringDeduplication \
    -Djava.security.egd=file:/dev/./urandom"

# Punto de entrada con opciones JVM
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
