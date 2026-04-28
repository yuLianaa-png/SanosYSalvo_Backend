# Usa una imagen base de Java compatible con la versión que compilaste
FROM eclipse-temurin:17-jdk-alpine
# Define el nombre del archivo JAR generado por Maven
ARG JAR_FILE=target/*.jar
# Copia el JAR al contenedor
COPY ${JAR_FILE} app.jar
# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app.jar"]