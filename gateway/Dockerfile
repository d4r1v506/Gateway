# Usar una imagen base de Tomcat con JDK 8
FROM openjdk:8-jdk-alpine
# Crear un directorio en el contenedor para almacenar el archivo JAR
WORKDIR /app

# Copiar el archivo JAR desde la carpeta local al contenedor
COPY target/gateway.jar /app/gateway.jar

# Exponer el puerto en el que la aplicación Spring Boot estará escuchando
EXPOSE 9000

# Ejecutar el archivo JAR usando java
CMD ["java", "-jar", "gateway.jar"]